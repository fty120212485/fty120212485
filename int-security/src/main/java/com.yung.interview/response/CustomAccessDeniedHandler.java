package com.yung.interview.response;

import cn.hutool.json.JSONUtil;
import com.yung.interview.response.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * description:自定义没有权限的响应信息
 *
 * @author: fengtianyong
 * @data: 2020/12/18 23:15
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println(JSONUtil.parse(CommonResult.unAuthorization()));
        response.getWriter().flush();
    }
}
