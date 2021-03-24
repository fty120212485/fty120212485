package com.yung.interview.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.yung.interview.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2021/1/6 19:57
 */
@Service
public class UploadServiceImpl implements UploadService {

    private final Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Value("${aliyun.oss.accessKeyId}")
    private String accessId;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKey;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.bucket-name}")
    private String bucket;
    @Value("${aliyun.oss.callbackUrl}")
    private String callbackUrl;
    @Value("${aliyun.oss.dir-prefix}")
    private String prefix;
    @Value("${aliyun.oss.expireTime}")
    private long expireTime;

    @Override
    public Map policy(String path) {
        String dir = prefix + path;
        String host = "http://" + bucket + "." + endpoint;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
        try {
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessId", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));

            Map callback = new HashMap();
            callback.put("callbackUrl", callbackUrl);
            callback.put("callbackBody",
                    "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            callback.put("callbackBodyType", "application/x-www-form-urlencoded");
            String base64CallbackBody = BinaryUtil.toBase64String(callback.toString().getBytes());
            //respMap.put("callback", base64CallbackBody);
            return respMap;
        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            logger.error(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return null;
    }
}
