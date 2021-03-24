package com.yung.interview.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2021/1/13 16:01
 */
public class ClaimsUtil {

    public static String getUserName(HttpServletRequest request){
        //Claims claims = (Claims) request.getAttribute("username");
        String username = (String)request.getAttribute("username");
        return  username == null?"":username;
    }
}
