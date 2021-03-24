package com.yung.interview.dao;

import com.yung.interview.model.Menu;
import com.yung.interview.model.Resource;
import com.yung.interview.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description: 自定义角色管理持久层接口
 *
 * @author: fengtianyong
 * @data: 2021/1/13 14:59
 */
public interface RoleDao {

    /**
     * 根据adminId获取角色
     * 只获取角色编码，以逗号隔开
     * @param managerId
     * @return
     */
    List<Role> getRoleByManagerId(@Param("managerId") int managerId);

    /**
     * 根据adminId获取菜单列表
     * @param managerId
     * @return
     */
    List<Menu> getMenuListByManagerId(@Param("managerId") int managerId);

    /**
     * 根据角色Id获取菜单列表
     * @param roleId
     */
    List<Menu> getMenuListByRoleId(@Param("roleId") int roleId);

    /**
     * 根据资源Id获取角色
     * 只获取角色编码，以逗号隔开
     * @param resourceId
     */
    String getRoleStrByResourceId(@Param("resourceId") int resourceId);

    /**
     * 据角色Id获取资源列表
     * @param roleId
     * @return
     */
    List<Resource> getResourceListByRoleId(@Param("roleId") int roleId);

    int changeStatus(@Param("roleId") int roleId, @Param("newStatus") int newStatus);

    int unbindMenuBatch(@Param("roleId") int roleId, @Param("menuIds") List<Integer> menuIds);

    int bindMenuBatch(@Param("roleId") int roleId, @Param("menuIds") List<Integer> menuIds);

    int unbindResourceBatch(@Param("roleId") int roleId, @Param("resourceIds") List<Integer> resourceIds);

    int bindResourceBatch(@Param("roleId") int roleId, @Param("resourceIds") List<Integer> resourceIds);
}
