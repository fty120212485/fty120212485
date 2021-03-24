package com.yung.interview.response;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2020/12/17 23:04
 */
public class CommonResult {

    private Integer code;
    private String message;
    private Object data;

    protected CommonResult(){}
    protected CommonResult(Integer code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static CommonResult success(String message, Object data){
        return new CommonResult(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static CommonResult success(String message){
        return success(message, null);
    }

    public static CommonResult success(Object data){
        return success(ResultCode.SUCCESS.getMessage(), data);
    }

    public static CommonResult success(){
        return success(ResultCode.SUCCESS.getMessage(),null);
    }

    public static CommonResult failed(Integer code, String message){
        return new CommonResult(code, message,null);
    }

    public static CommonResult failed(String message){
        return failed(ResultCode.FAILED.getCode(), message);
    }

    public static CommonResult validateFailed(){
        return failed(ResultCode.VALIDATE_FAILED.getCode(), ResultCode.VALIDATE_FAILED.getMessage());
    }

    public static CommonResult validateFailed(String message){
        return failed(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    public static CommonResult unAuthentication(){
        return failed(ResultCode.UN_AUTHENTICATION.getCode(), ResultCode.UN_AUTHENTICATION.getMessage());
    }

    public static CommonResult unAuthorization(){
        return failed(ResultCode.UN_AUTHORIZATION.getCode(), ResultCode.UN_AUTHORIZATION.getMessage());
    }

    public Integer getCode() {
        return code;
    }

    public CommonResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommonResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public CommonResult setData(Object data) {
        this.data = data;
        return this;
    }
}
