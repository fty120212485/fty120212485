package com.yung.interview.service;

import com.yung.interview.model.Manager;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * description: 后台用户管理业务层接口
 *
 * @author: fengtianyong
 * @data: 2020/12/9 21:26
 */
public interface ManagerService {

    String login(String username, String password);

    Manager getAdmin(String username);

    UserDetails loadUserByUsername(String username);

    List<Manager> list(String keyword);

    int add(Manager manager);

    int update(Manager manager);

    int delete(int managerId);

    int status(int managerId);

    void assignRole(int managerId, int[] roles);
}
