package com.yung.interview.service.impl;

import com.yung.interview.mapper.MenuMapper;
import com.yung.interview.model.Menu;
import com.yung.interview.model.MenuExample;
import com.yung.interview.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2021/1/15 4:08
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> list(Menu menus) {
        MenuExample example = new MenuExample();
        MenuExample.Criteria criteria = example.createCriteria();
        if(menus.getLevel() != null){
            criteria.andLevelEqualTo(menus.getLevel());
        }

        if(menus.getParent() != null){
            criteria.andParentEqualTo(menus.getParent());
        }

        List<Menu> menuList = menuMapper.selectByExample(example);
        if (menuList.isEmpty()){
            return new ArrayList<>();
        }
        return menuList;
    }

    @Override
    public int add(Menu menu){
        menu.setCreateTime(new Date());
        menu.setUpdateTime(new Date());
        int insert = menuMapper.insert(menu);
        return insert;
    }

    @Override
    public int update(Menu menu){
        menu.setUpdateTime(new Date());
        int result = menuMapper.updateByPrimaryKeySelective(menu);
        return result;
    }

    @Override
    public int delete(int menuId){
        int result = menuMapper.deleteByPrimaryKey(menuId);
        return result;
    }

    @Override
    public int status(int menuId) {
        Menu menu = menuMapper.selectByPrimaryKey(menuId);
        if(menu != null){
            Byte status = menu.getStatus();
            int newStatus = 0;
            if(status == 0){
                newStatus = 1;
            }
            MenuExample menuExample = new MenuExample();
            MenuExample.Criteria criteria = menuExample.createCriteria();
            criteria.andIdEqualTo(menuId);
            criteria.andStatusEqualTo(menu.getStatus());//旧状态
            menu.setStatus(Byte.valueOf(Integer.toString(newStatus)));//新状态
            int result = menuMapper.updateByExampleSelective(menu, menuExample);
            return result;
        }
        return 0;
    }

    @Override
    public List<Map> getTreeMenu(){
        MenuExample example = new MenuExample();
        List<Menu> menus = menuMapper.selectByExample(example);
        //1.先封装树形最外层结构
        List<Map> treeMenuList = new ArrayList<>();
        for (Menu menu : menus) {
            Map<String, Object> outerTree = new HashMap<>();
            if(menu.getLevel() == 1){
                outerTree.put("id", menu.getId());
                outerTree.put("label", menu.getTitle());
                outerTree.put("children", null);
                treeMenuList.add(outerTree);
            }
        }
        //2.封装树形内层结构
        treeMenuList.forEach(item ->{
            List<Map> childrenList = new ArrayList();
            for (Menu menu : menus) {
                if (menu.getParent().equals(item.get("id"))) {
                    Map<String, Object> innerTree = new HashMap<>();
                    innerTree.put("id", menu.getId());
                    innerTree.put("label", menu.getTitle());
                    //outerTree.put("children", null);  目前最多两级菜单
                    childrenList.add(innerTree);
                }
            }
            item.put("children", childrenList);
        });
        return treeMenuList;
    }
}
