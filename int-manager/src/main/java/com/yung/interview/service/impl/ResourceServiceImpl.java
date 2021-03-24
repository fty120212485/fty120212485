package com.yung.interview.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.yung.interview.dao.ResourceDao;
import com.yung.interview.exception.ApiException;
import com.yung.interview.mapper.ResourceCategoryMapper;
import com.yung.interview.mapper.ResourceMapper;
import com.yung.interview.model.Resource;
import com.yung.interview.model.ResourceCategory;
import com.yung.interview.model.ResourceCategoryExample;
import com.yung.interview.model.ResourceExample;
import com.yung.interview.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2021/1/2 21:10
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceDao resourceDao;
    @Autowired
    private ResourceCategoryMapper categoryMapper;
    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public List<Resource> listAll(){
        List<Resource> resources = resourceMapper.selectByExample(null);
        if(CollectionUtils.isEmpty(resources)){
            return new ArrayList<>();
        }
        return resources;
    }

    @Override
    public ResourceCategory findCategoryByCtrl(String ctrl) {
        if(StringUtils.isEmpty(ctrl)){
            throw new ApiException("缺少参数ctrl");
        }
        ResourceCategoryExample categoryExample = new ResourceCategoryExample();
        ResourceCategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andControllerEqualTo(ctrl);
        List<ResourceCategory> categoryList = categoryMapper.selectByExample(categoryExample);
        if(!categoryList.isEmpty()){
            return categoryList.get(0);
        }
        return null;
    }

    @Override
    public List<ResourceCategory> categoryList(){
        return categoryMapper.selectByExample(new ResourceCategoryExample());
    }

    @Override
    public int insertCategory(ResourceCategory resourceCategory) {
        resourceCategory.setCreateTime(new Date());
        resourceCategory.setUpdateTime(new Date());
        return categoryMapper.insert(resourceCategory);
    }

    @Override
    public void batchInsert(List<Resource> resourceList) {
        resourceDao.batchInsert(resourceList);
    }

    @Override
    public int status(int resourceId){
        Resource resource = resourceMapper.selectByPrimaryKey(resourceId);
        if(resource != null){
            Byte status = resource.getStatus();
            int newStatus = 0;
            if(status == 0){
                newStatus = 1;
            }
            ResourceExample resourceExample = new ResourceExample();
            ResourceExample.Criteria criteria = resourceExample.createCriteria();
            criteria.andIdEqualTo(resourceId);
            criteria.andStatusEqualTo(resource.getStatus());//旧状态
            resource.setStatus(Byte.valueOf(Integer.toString(newStatus)));//新状态
            int result = resourceMapper.updateByExampleSelective(resource, resourceExample);
            return result;
        }
        return 0;
    }
}
