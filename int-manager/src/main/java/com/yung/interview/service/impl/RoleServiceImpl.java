package com.yung.interview.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.yung.interview.dao.RoleDao;
import com.yung.interview.exception.ApiException;
import com.yung.interview.mapper.RoleMapper;
import com.yung.interview.model.Menu;
import com.yung.interview.model.Resource;
import com.yung.interview.model.Role;
import com.yung.interview.model.RoleExample;
import com.yung.interview.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * description: 角色管理业务层实现类
 *
 * @author: fengtianyong
 * @data: 2021/1/13 16:21
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoleByManagerId(int managerId) {
        if(managerId == 0){
            throw new ApiException("缺少参数managerId");
        }
        List<Role> role = roleDao.getRoleByManagerId(managerId);
        return role;
    }

    @Override
    public List<Menu> getMenuListByManagerId(int managerId) {
        return roleDao.getMenuListByManagerId(managerId);
    }

    @Override
    public List<Menu> getMenuListByRoleId(int roleId) {
        if(roleId == 0){
            throw new ApiException("缺少参数roleId");
        }
        return roleDao.getMenuListByRoleId(roleId);
    }

    @Override
    public List<Resource> getResourceListByRoleId(int roleId) {
        if(roleId == 0){
            throw new ApiException("缺少参数roleId");
        }
        return roleDao.getResourceListByRoleId(roleId);
    }

    @Override
    public String getRoleStrByResourceId(int resourceId) {
        return roleDao.getRoleStrByResourceId(resourceId);
    }

    @Override
    public List<Role> list(Role role) {
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        if(!StringUtils.isEmpty(role.getName())){
            criteria.andCodeEqualTo(role.getName());
        }
        List<Role> roles = roleMapper.selectByExample(roleExample);
        if(roles.isEmpty()){
            return new ArrayList<>();
        }
        return roles;
    }

    @Override
    public int add(Role role) {
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        int insert = roleMapper.insert(role);
        return insert;
    }

    @Override
    public int update(Role role) {
        String code = role.getCode();
        Byte status = role.getStatus();
        if(!StringUtils.isEmpty(code) && code.equals("superAdministrator") &&
                status != null && status == 0){
            throw new ApiException("超级管理员不允许停用");
        }
        role.setUpdateTime(new Date());
        int result = roleMapper.updateByPrimaryKeySelective(role);
        return result;
    }

    @Override
    public int delete(int roleId) {
        Role role = roleMapper.selectByPrimaryKey(roleId);
        if(role == null){
            throw new ApiException("该角色不存在");
        }
        if(role.getCode().equals("superAdministrator")){
            throw new ApiException("超级管理员不允许删除");
        }
        int result = roleMapper.deleteByPrimaryKey(roleId);
        return result;
    }

    @Override
    public int status(int roleId) {
        Role role = roleMapper.selectByPrimaryKey(roleId);
        if(role != null){
            if(role.getCode().equals("superAdministrator")){
                throw new ApiException("超级管理员不允许停用");
            }
            Byte status = role.getStatus();
            int newStatus = 0;
            if(status == 0){
                newStatus = 1;
            }
            RoleExample resourceExample = new RoleExample();
            RoleExample.Criteria criteria = resourceExample.createCriteria();
            criteria.andIdEqualTo(roleId);
            criteria.andStatusEqualTo(role.getStatus());//旧状态
            role.setStatus(Byte.valueOf(Integer.toString(newStatus)));//新状态
            int result = roleMapper.updateByExampleSelective(role, resourceExample);
            return result;
        }
        return 0;
    }

    @Override
    public void assignMenu(int roleId, int[] menuIds) {
        List<Menu> menuList = getMenuListByRoleId(roleId);
        List<Integer> unbindMenu = new ArrayList<>();
        List<Integer> bindMenu = new ArrayList<>();
        if(menuIds == null || menuIds.length == 0){
            for (Menu menu : menuList){
                unbindMenu.add(menu.getId());
            }
        }else{
            for (int menuId : menuIds) {
                bindMenu.add(menuId);
            }
            for (Menu menu : menuList){
                if (bindMenu.contains(menu.getId())) {
                    bindMenu.remove(menu.getId());
                }else{
                    unbindMenu.add(menu.getId());
                }
            }
        }
        if (!unbindMenu.isEmpty()) roleDao.unbindMenuBatch(roleId, unbindMenu);
        if (!bindMenu.isEmpty()) roleDao.bindMenuBatch(roleId, bindMenu);
    }

    @Override
    public void assignResource(int roleId, int[] resourceIds) {
        List<Resource> resourceList = getResourceListByRoleId(roleId);
        List<Integer> unbindResource = new ArrayList<>();
        List<Integer> bindResource = new ArrayList<>();
        if(resourceIds == null || resourceIds.length == 0){
            for (Resource resource : resourceList){
                unbindResource.add(resource.getId());
            }
        }else{
            for (int resourceId : resourceIds) {
                bindResource.add(resourceId);
            }
            for (Resource resource : resourceList){
                if (bindResource.contains(resource.getId())) {
                    bindResource.remove(resource.getId());
                }else{
                    unbindResource.add(resource.getId());
                }
            }
        }
        if (!unbindResource.isEmpty()) roleDao.unbindResourceBatch(roleId, unbindResource);
        if (!bindResource.isEmpty()) roleDao.bindResourceBatch(roleId, bindResource);
    }
}
