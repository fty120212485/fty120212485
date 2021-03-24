package com.yung.interview.authentication;

import com.yung.interview.DynamicLoadDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.*;

/**
 * description:获取路径资源的角色权限
 *
 * @author: fengtianyong
 * @data: 2020/12/13 20:50
 */
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private DynamicLoadDataService dynamicLoadDataService;

    private Map<String, ConfigAttribute> configurationMap;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if(configurationMap == null){
            configurationMap = dynamicLoadDataService.loadDataSource();
        }
        List<ConfigAttribute> config = new ArrayList<>();
        String url = ((FilterInvocation) object).getRequestUrl();
        String path = "";
        configurationMap.forEach((k,v)->{
            if(url.startsWith(k)){
                config.add(v);
            }
        });
        return config;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
