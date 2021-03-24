package com.yung.interview.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description: 自定义后台用户管理持久层接口
 *
 * @author: fengtianyong
 * @data: 2021/1/19 1:59
 */
public interface ManagerDao {

    int unbindRole(@Param("managerId") int managerId,@Param("roleIds") List<Integer> roleIds);

    int bindRole(@Param("managerId") int managerId,@Param("roleIds") List<Integer> roleIds);
}
