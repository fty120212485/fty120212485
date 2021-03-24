package com.yung.interview.service;

import com.yung.interview.model.Menu;

import java.util.List;
import java.util.Map;

/**
 * description: 菜单管理业务层接口
 *
 * @author: fengtianyong
 * @data: 2021/1/15 4:04
 */
public interface MenuService {

    List<Menu> list(Menu menus);

    int add(Menu menu);

    int update(Menu menu);

    int delete(int menuId);

    int status(int menuId);

    List<Map> getTreeMenu();
}
