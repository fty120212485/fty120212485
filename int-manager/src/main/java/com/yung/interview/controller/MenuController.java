package com.yung.interview.controller;

import com.yung.interview.response.CommonResult;
import com.yung.interview.model.Menu;
import com.yung.interview.service.MenuService;
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
 * @data: 2021/1/15 4:02
 */
@Api(value = "MenuController", tags = "菜单管理")
@RestController
@RequestMapping("/back/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation("菜单列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult list(Menu menu){
        return CommonResult.success(menuService.list(menu));
    }

    @ApiOperation("新增菜单")
    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public CommonResult add(@RequestBody Menu menu){
        return CommonResult.success(menuService.add(menu));
    }

    @ApiOperation("修改菜单")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult update(@RequestBody Menu menu){
        return CommonResult.success(menuService.update(menu));
    }

    @ApiOperation("删除菜单")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public CommonResult delete(int menuId){
        return CommonResult.success(menuService.delete(menuId));
    }

    @ApiOperation("修改菜单状态")
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public CommonResult status(int menuId){
        return CommonResult.success(menuService.status(menuId));
    }

    @ApiOperation("获取树形菜单")
    @RequestMapping(value = "/getTreeMenu", method = RequestMethod.GET)
    public CommonResult getTreeMenu(){
        return CommonResult.success(menuService.getTreeMenu());
    }
}
