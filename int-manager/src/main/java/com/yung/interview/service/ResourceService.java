package com.yung.interview.service;

import com.yung.interview.model.Resource;
import com.yung.interview.model.ResourceCategory;

import java.util.List;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2021/1/2 21:10
 */
public interface ResourceService {

    /**
     * 获取所有资源
     * @return
     */
    List<Resource> listAll();

    ResourceCategory findCategoryByCtrl(String controller);

    int insertCategory(ResourceCategory resourceCategory);

    void batchInsert(List<Resource> resourceList);

    int status(int resourceId);

    List<ResourceCategory> categoryList();
}
