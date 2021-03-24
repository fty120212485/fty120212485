package com.yung.interview;

import com.yung.interview.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.security.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2020/12/9 15:54
 */
public class JWTUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATE_TIME = "create_time";
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.secret.private}")
    private  String SECRET_PRIVATE_KEY;
    @Value("${jwt.secret.public}")
    private  String SECRET_PUBLIC_KEY;
    private  PrivateKey privateKey;
    private PublicKey publicKey;
    @Autowired
    private RedisService redisService;

    private void initSecretKey(){
        if(redisService.exists(SECRET_PUBLIC_KEY) || redisService.exists(SECRET_PRIVATE_KEY)){
            this.publicKey = (PublicKey) redisService.get(SECRET_PUBLIC_KEY);
            this.privateKey = (PrivateKey) redisService.get(SECRET_PRIVATE_KEY);
        }else{
            KeyPairGenerator kpg = null;
            KeyPair keyPair = null;
            try{
                kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(2048);
                keyPair = kpg.genKeyPair();
                this.privateKey = keyPair.getPrivate();
                this.publicKey = keyPair.getPublic();
                redisService.set(SECRET_PRIVATE_KEY , this.privateKey);
                redisService.set(SECRET_PUBLIC_KEY, this.publicKey);
            }catch (NoSuchAlgorithmException e){
                LOGGER.error("获取secret key失败", e);
            }
        }
    }
    private PrivateKey privateKey(){
        if(this.privateKey == null){
            initSecretKey();
        }
        return this.privateKey;
    }
    private PublicKey publicKey(){
        if(this.publicKey == null){
            initSecretKey();
        }
        return this.publicKey;
    }

    private String generateToken(Map<String, Object> claims){
        return Jwts.builder()
                .signWith(privateKey())
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    public Claims parseToken(String token){
        Claims claims = null;
        try{
            claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey())
                    .build()
                    .parseClaimsJws(token).getBody();
        }catch (Exception e){
            LOGGER.info("jwt解析失败");
        }
        return claims;
    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATE_TIME, new Date());
        return generateToken(claims);
    }


    //从token中获取用户名
    public String getUserNameByToken(String token){
        Claims claims = parseToken(token);
        return claims != null?(String) claims.get(CLAIM_KEY_USERNAME): null;
    }
}
