package com.yung.interview.service;

import com.yung.interview.model.Menu;
import com.yung.interview.model.Resource;
import com.yung.interview.model.Role;

import java.util.List;

/**
 * description: 角色管理业务层接口
 *
 * @author: fengtianyong
 * @data: 2021/1/13 16:14
 */
public interface RoleService {

    /**
     * 根据adminId获取角色
     * 只获取角色编码，以逗号隔开
     */
    List<Role> getRoleByManagerId(int managerId);

    /**
     * 根据adminId获取菜单列表
     */
    List<Menu> getMenuListByManagerId(int managerId);

    /**
     * 根据角色Id获取菜单列表
     */
    List<Menu> getMenuListByRoleId(int roleId);

    /**
     * 根据角色Id获取资源列表
     */
    List<Resource> getResourceListByRoleId(int roleId);

    /**
     * 根据资源Id获取角色
     * 只获取角色编码，以逗号隔开
     */
    String getRoleStrByResourceId(int resourceId);

    List<Role> list(Role role);

    int add(Role role);

    int update(Role role);

    int delete(int roleId);

    int status(int roleId);

    void assignMenu(int roleId, int[] menuIds);

    void assignResource(int roleId, int[] resourceIds);
}
