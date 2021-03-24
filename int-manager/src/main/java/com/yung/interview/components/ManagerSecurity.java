package com.yung.interview.components;

import com.alibaba.druid.util.StringUtils;
import com.yung.interview.AdminAuthentication;
import com.yung.interview.DynamicLoadDataService;
import com.yung.interview.model.Resource;
import com.yung.interview.service.ManagerService;
import com.yung.interview.service.ResourceService;
import com.yung.interview.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2020/12/12 22:25
 */
@Configuration
public class ManagerSecurity {

    @Autowired
    private ManagerService managerService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RoleService roleService;

    @Bean
    public UserDetailsService loadUserByUsername() {
        return username -> managerService.loadUserByUsername(username);
    }

    @Bean
    public DynamicLoadDataService dynamicLoadDataService(){
        return () -> {
            //功能：从数据库加载所有资源，并且获取资源的角色权限
            //实现：1.保证数据库存在所有资源数据。1.1：数据初始化 1.2：插入更新数据
            //     2.获取所有资源信息，根据资源得到对应的角色权限
            //     2.1 获取所有资源url
            Map<String, ConfigAttribute> configurationMap = new HashMap<>();
            List<Resource> resources = resourceService.listAll();
            for (Resource resource : resources) {
                //2.2 获取资源权限
                String roleStr = roleService.getRoleStrByResourceId(resource.getId());
                Byte status = resource.getStatus();
                if (StringUtils.isEmpty(roleStr) || Byte.parseByte(Integer.toString(1)) != status) {
                    roleStr = "can't request";
                }
                configurationMap.put(resource.getUrl(), new org.springframework.security.access.SecurityConfig(roleStr));
            }
            return configurationMap;
        };
    }

    @Bean
    public AdminAuthentication adminAuthentication(){
        return authenticateUser -> managerService.getAdmin(authenticateUser).getUsername();
    }
}

