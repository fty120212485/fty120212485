package com.yung.interview.exception;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2020/12/19 0:20
 */
public class Asserts {

    public static void fail(String message){
        throw new ApiException(message);
    }

    public static void checkUserName(){
        throw new ApiException("用户名不能为空");
    }
}
