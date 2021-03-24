package com.yung.interview.exception;

import com.yung.interview.response.ResultCode;

/**
 * description: 自定义校验异常
 *
 * @author: fengtianyong
 * @data: 2020/12/19 0:01
 */
public class ApiException extends RuntimeException {

    private ResultCode resultCode;

    public ApiException(ResultCode resultCode){
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ApiException(String message){
        super(message);
    }

    public ApiException(Throwable cause, String message){
        super(message, cause);
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public ApiException setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
        return this;
    }
}
