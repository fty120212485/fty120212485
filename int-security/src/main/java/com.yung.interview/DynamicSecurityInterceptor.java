package com.yung.interview;

import com.yung.interview.authentication.DynamicAccessDecisionManager;
import com.yung.interview.authentication.DynamicSecurityMetadataSource;
import com.yung.interview.utils.ClaimsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2020/12/13 20:24
 */
public class DynamicSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    @Autowired
    private IgnoreUrlConfig ignoreUrlConfig;
    @Autowired
    private DynamicSecurityMetadataSource dynamicSecurityMetadataSource;
    @Autowired
    private AdminAuthentication adminAuthentication;

    @Autowired
    public void setAccessDecisionManager(DynamicAccessDecisionManager dynamicAccessDecisionManager) {
        super.setAccessDecisionManager(dynamicAccessDecisionManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, filterChain);
        //超级管理员直接跳过认证
        String username = adminAuthentication.getUserName(ClaimsUtil.getUserName((HttpServletRequest) request));
        if("admin".equals(username)){
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            return;
        }
        //白名单直接跳过权限认证
        String requestUrl = fi.getRequestUrl();
        for (String url : ignoreUrlConfig.urls) {
            if(url.matches(requestUrl)){
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
                return;
            }
        }
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try{
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        }finally{
            super.afterInvocation(token, null);
        }
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return dynamicSecurityMetadataSource;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
