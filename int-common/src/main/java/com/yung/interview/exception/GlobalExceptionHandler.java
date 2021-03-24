package com.yung.interview.exception;

import com.yung.interview.response.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * description: 全局异常处理逻辑类
 *
 * @author: fengtianyong
 * @data: 2020/12/18 23:58
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public CommonResult apiException(ApiException e){
        if(e.getResultCode() != null){
            return CommonResult.failed(e.getResultCode().getCode(), e.getResultCode().getMessage());
        }
        return CommonResult.failed(e.getMessage());
    }
    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public CommonResult exception(RuntimeException e){
        e.printStackTrace();
        return CommonResult.failed("系统出错！");
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResult methodArgumentNotValidateException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        if(bindingResult.hasErrors()){
            FieldError fieldError = bindingResult.getFieldError();
            if(fieldError != null)
                return CommonResult.validateFailed(fieldError.getDefaultMessage());
        }
        return CommonResult.validateFailed();
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public CommonResult bindException(BindException e){
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null)
                return CommonResult.validateFailed(fieldError.getDefaultMessage());
        }
        return CommonResult.validateFailed();
    }
}
