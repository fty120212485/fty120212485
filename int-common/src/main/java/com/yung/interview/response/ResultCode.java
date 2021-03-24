package com.yung.interview.response;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2020/12/17 23:41
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数检验失败"),
    UN_AUTHENTICATION(401, "未登陆或token已经过期"),
    UN_AUTHORIZATION(403, "您没有相关权限");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
