package com.yung.interview.controller;

import com.yung.interview.model.Menu;
import com.yung.interview.response.CommonResult;
import com.yung.interview.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2021/1/4 15:00
 */
@Api(value = "ResourceController", tags = "资源管理")
@RestController
@RequestMapping("/back/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @ApiOperation("资源列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult list(){
        return CommonResult.success(resourceService.listAll());
    }

    @ApiOperation("修改资源状态")
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public CommonResult status(int resourceId){
        return CommonResult.success(resourceService.status(resourceId));
    }

    @ApiOperation("资源分类列表")
    @RequestMapping(value = "/categoryList", method = RequestMethod.GET)
    public CommonResult categoryList(){
        return CommonResult.success(resourceService.categoryList());
    }
}
