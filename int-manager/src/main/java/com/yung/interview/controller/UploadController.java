package com.yung.interview.controller;

import com.yung.interview.response.CommonResult;
import com.yung.interview.service.UploadService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2021/1/6 16:42
 */
@RestController
@RequestMapping("/back/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @CrossOrigin
    @ApiOperation("oss上传签名")
    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    public CommonResult policy(String path){
        Map result = uploadService.policy(path);
        return CommonResult.success(result);
    }

    @ApiOperation("oss上传回调")
    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public CommonResult callback(){
        return CommonResult.success(null);
    }
}
