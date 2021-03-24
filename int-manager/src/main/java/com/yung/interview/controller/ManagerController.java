package com.yung.interview.controller;

import com.yung.interview.exception.ApiException;
import com.yung.interview.model.Manager;
import com.yung.interview.model.Menu;
import com.yung.interview.model.Role;
import com.yung.interview.response.CommonResult;
import com.yung.interview.service.ManagerService;
import com.yung.interview.service.MenuService;
import com.yung.interview.service.RoleService;
import com.yung.interview.utils.ClaimsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2020/12/9 21:24
 */
@Api(value = "ManagerController", tags = "用户管理")
@RestController
@RequestMapping("/back/manager")
public class ManagerController {

    @Value("${jwt.head}")
    private String token_header;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    @ApiOperation("用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult login(String username, String password){
        String token = managerService.login(username, password);
        if(token == null){
            return CommonResult.validateFailed("用户名或密码错误！");
        }
        Map map = new HashMap<String, String>();
        map.put("token_head", "Bearer ");
        map.put("token", token);
        return CommonResult.success(map);
    }

    @ApiOperation("用户注销")
    @RequestMapping(value="/logout", method=RequestMethod.GET)
    public CommonResult logout(){
        return CommonResult.success("quit success");
    }

    @ApiOperation("用户信息")
    @RequestMapping(value="/info", method = RequestMethod.GET)
    public CommonResult info(HttpServletRequest request){
        String username = ClaimsUtil.getUserName(request);
        Map<String, Object> map = new HashMap<>();
        Manager manager = managerService.getAdmin(username);
        List<Role> roles = roleService.getRoleByManagerId(manager.getId());
        boolean isAdmin = false;
        for (Role role : roles){
            if(role.getCode().equals("superAdministrator"))
                isAdmin = true;
        }
        List<Menu> menus = null;
        if(isAdmin){
            menus = menuService.list(new Menu());
        }else{
            menus = roleService.getMenuListByManagerId(manager.getId());
        }
        if(manager == null){
            throw new ApiException("账号已注销！");
        }
        map.put("info", manager);
        map.put("menu", menus);
        return CommonResult.success(map);
    }

    @ApiOperation("用户列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult list(String keyword){
        return CommonResult.success(managerService.list(keyword));
    }

    @ApiOperation("新增用户")
    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public CommonResult add(@RequestBody Manager manager){
        return CommonResult.success(managerService.add(manager));
    }

    @ApiOperation("修改用户")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult update(@RequestBody Manager manager){
        return CommonResult.success(managerService.update(manager));
    }

    @ApiOperation("删除用户")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public CommonResult delete(int managerId){
        if(managerId == 0){
            throw new ApiException("缺少参数managerId");
        }
        return CommonResult.success(managerService.delete(managerId));
    }

    @ApiOperation("修改用户状态")
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public CommonResult status(int managerId){
        if(managerId == 0){
            throw new ApiException("缺少参数managerId");
        }
        return CommonResult.success(managerService.status(managerId));
    }

    @ApiOperation("获取指定用户的角色")
    @RequestMapping(value = "/getRole", method = RequestMethod.GET)
    public CommonResult role(int managerId){
        return CommonResult.success(roleService.getRoleByManagerId(managerId));
    }

    @ApiOperation("分配角色")
    @RequestMapping(value = "/assignRole", method = RequestMethod.GET)
    public CommonResult assignRole(int managerId, int[] roleIds){
        if(managerId == 0){
            throw new ApiException("缺少参数managerId");
        }
        managerService.assignRole(managerId, roleIds);
        return CommonResult.success();
    }
}
