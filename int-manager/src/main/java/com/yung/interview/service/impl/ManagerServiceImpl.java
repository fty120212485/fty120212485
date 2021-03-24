package com.yung.interview.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.yung.interview.JWTUtil;
import com.yung.interview.dao.ManagerDao;
import com.yung.interview.dao.RoleDao;
import com.yung.interview.exception.ApiException;
import com.yung.interview.exception.Asserts;
import com.yung.interview.mapper.ManagerMapper;
import com.yung.interview.mapper.ManagerRoleMapper;
import com.yung.interview.model.Manager;
import com.yung.interview.model.ManagerExample;
import com.yung.interview.model.ManagerExample.Criteria;
import com.yung.interview.model.ManagerRole;
import com.yung.interview.model.Role;
import com.yung.interview.service.ManagerService;
import com.yung.interview.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * description: 后台用户管理业务层实现类
 *
 * @author: fengtianyong
 * @data: 2020/12/9 21:27
 */
@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private ManagerMapper managerMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ManagerDao managerDao;

    @Override
    public String login(String username, String password) {
        if(username.isEmpty()){
            Asserts.checkUserName();
        }
        UserDetails userDetails = loadUserByUsername(username);
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new ApiException("用户名或密码不正确");
        }
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(),
                        userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        return jwtUtil.generateToken(username);
    }

    @Override
    public Manager getAdmin(String username){
        ManagerExample adminExample = new ManagerExample();
        Criteria criteria = adminExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<Manager> managers = managerMapper.selectByExample(adminExample);
        if(!managers.isEmpty()){
            return managers.get(0);
        }
        return new Manager();
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        Manager manager = getAdmin(username);
        if(StringUtils.isEmpty(manager.getUsername()) || manager.getStatus() == 0){
            throw new ApiException("账号不存在或未激活");
        }
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();
        List<Role> roleList = roleDao.getRoleByManagerId(manager.getId());
        String roleStr = "";
        if(!roleList.isEmpty()){
            for (int i = 0; i < roleList.size(); i++) {
                simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(roleList.get(i).getCode()));
            }
        }
        return new User(username, manager.getPassword(), simpleGrantedAuthorityList);
    }

    @Override
    public List<Manager> list(String keyword) {
        ManagerExample managerExample = new ManagerExample();
        Criteria criteria = managerExample.createCriteria();
        if(!StringUtils.isEmpty(keyword)){
            criteria.andUsernameEqualTo("%"+keyword+"%");
            managerExample.or(criteria.andNicknameEqualTo("%"+keyword+"%"));
        }
        List<Manager> managers = managerMapper.selectByExample(managerExample);
        if(managers.isEmpty()){
            return new ArrayList<>();
        }
        return managers;
    }

    @Override
    public int add(Manager manager) {
        manager.setCreateTime(new Date());
        manager.setUpdateTime(new Date());
        int insert = managerMapper.insert(manager);
        return insert;
    }

    @Override
    public int update(Manager manager) {
        String username = manager.getUsername();
        Byte status = manager.getStatus();
        if(!StringUtils.isEmpty(username) && username.equals("admin") &&
                status != null && status == 0){
            throw new ApiException("超级管理员不允许停用");
        }
        manager.setUpdateTime(new Date());
        int result = managerMapper.updateByPrimaryKeySelective(manager);
        return result;
    }

    @Override
    public int delete(int managerId) {
        Manager manager = managerMapper.selectByPrimaryKey(managerId);
        if(manager == null){
            throw new ApiException("该用户不存在");
        }
        if(manager.getUsername().equals("admin")){
            throw new ApiException("超级管理员不允许删除");
        }
        int result = managerMapper.deleteByPrimaryKey(managerId);
        return result;
    }

    @Override
    public int status(int managerId){
        Manager manager = managerMapper.selectByPrimaryKey(managerId);
        if(manager != null){
            if(manager.getUsername().equals("admin")){
                throw new ApiException("超级管理员不允许停用");
            }
            Byte status = manager.getStatus();
            int newStatus = 0;
            if(status == 0){
                newStatus = 1;
            }
            ManagerExample resourceExample = new ManagerExample();
            ManagerExample.Criteria criteria = resourceExample.createCriteria();
            criteria.andIdEqualTo(managerId);
            criteria.andStatusEqualTo(manager.getStatus());//旧状态
            manager.setStatus(Byte.valueOf(Integer.toString(newStatus)));//新状态
            int result = managerMapper.updateByExampleSelective(manager, resourceExample);
            return result;
        }
        return 0;
    }

    @Override
    public void assignRole(int managerId, int[] roles){
        List<Role> roleList = roleDao.getRoleByManagerId(managerId);
        List<Integer> bindIds = new ArrayList<>();
        List<Integer> unbindIds = new ArrayList<>();
        if(roles == null || roles.length == 0){
            for (Role role : roleList){
                unbindIds.add(role.getId());
            }
        }else{
            for (int roleId : roles){
                bindIds.add(roleId);
            }
            for (Role role : roleList){
                if(bindIds.contains(role.getId())){
                    bindIds.remove(role.getId());
                }else{
                    unbindIds.add(role.getId());
                }
            }
        }
        if(!unbindIds.isEmpty()) managerDao.unbindRole(managerId,  unbindIds);//解绑
        if(!bindIds.isEmpty()) managerDao.bindRole(managerId,  bindIds);//绑定
    }
}
