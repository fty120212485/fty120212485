package com.yung.interview.controller;

import com.yung.interview.model.Menu;
import com.yung.interview.model.Resource;
import com.yung.interview.model.Role;
import com.yung.interview.response.CommonResult;
import com.yung.interview.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2021/1/16 14:34
 */
@Api(value = "RoleController", tags = "角色管理")
@RestController
@RequestMapping("/back/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation("角色列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult list(Role role){
        return CommonResult.success(roleService.list(role));
    }

    @ApiOperation("新增角色")
    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public CommonResult add(@RequestBody Role role){
        return CommonResult.success(roleService.add(role));
    }

    @ApiOperation("修改角色")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult update(@RequestBody Role role){
        return CommonResult.success(roleService.update(role));
    }

    @ApiOperation("删除角色")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public CommonResult delete(int roleId){
        return CommonResult.success(roleService.delete(roleId));
    }

    @ApiOperation("修改角色状态")
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public CommonResult status(int roleId){
        return CommonResult.success(roleService.status(roleId));
    }

    @ApiOperation("获取指定角色的菜单")
    @RequestMapping(value = "/getMenu", method = RequestMethod.GET)
    public CommonResult getMenu(int roleId){
        List<Menu> menuList = roleService.getMenuListByRoleId(roleId);
        return CommonResult.success(menuList);
    }

    @ApiOperation("分配菜单")
    @RequestMapping(value = "/assignMenu", method = RequestMethod.GET)
    public CommonResult assignMenu(int roleId, int[] menuIds){
        roleService.assignMenu(roleId, menuIds);
        return CommonResult.success();
    }

    @ApiOperation("获取指定角色的资源")
    @RequestMapping(value = "/getResource", method = RequestMethod.GET)
    public CommonResult getResource(int roleId){
        List<Resource> menuList = roleService.getResourceListByRoleId(roleId);
        return CommonResult.success(menuList);
    }

    @ApiOperation("分配资源")
    @RequestMapping(value = "/assignResource", method = RequestMethod.GET)
    public CommonResult assignResource(int roleId, int[] resourceIds){
        roleService.assignResource(roleId, resourceIds);
        return CommonResult.success();
    }
}
