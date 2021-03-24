package com.yung.interview.log;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2020/12/19 19:19
 */
@Component
@Aspect
public class LogAspect{

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut(value = "execution(public * com.yung.interview.controller.*.*(..))")
    public void pointcut(){}

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startMillis = System.currentTimeMillis();
        WebLog log = new WebLog();
        Object result = joinPoint.proceed();
        int spentMillis = (int)(System.currentTimeMillis() - startMillis);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Class clazz = joinPoint.getSignature().getDeclaringType();
        Method[] methods = clazz.getMethods();
        String methodName = joinPoint.getSignature().getName();
        Method currentMethod = null;
        for (Method method:methods){
            if(method.getName().equals(methodName)){
                currentMethod = method;
                ApiOperation annotation = method.getAnnotation(ApiOperation.class);
                if(annotation != null)
                    log.setDescription(annotation.value());
            }
        }
        log.setOperationTime(new Date(startMillis));
        log.setIp(request.getRemoteAddr());
        log.setResult(result);
        log.setSpentTime(spentMillis);
        log.setParameter(JSONUtil.toJsonStr(getParameter(joinPoint.getArgs(), currentMethod)));;
        logger.info("{}", JSONUtil.toJsonStr(log));
        return result;
    }

    private Map getParameter(Object[] args, Method currentMethod) {
        Parameter[] parameters = currentMethod.getParameters();
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < parameters.length; i++) {
           map.put(parameters[i].getName(), args[i]);
        }
        return map;
    }
}
