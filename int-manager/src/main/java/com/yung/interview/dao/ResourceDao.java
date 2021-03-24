package com.yung.interview.dao;

import com.yung.interview.model.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description: 自定义资源管理持久层接口
 *
 * @author: fengtianyong
 * @data: 2021/1/14 16:32
 */
public interface ResourceDao {
    /**
     * 批量插入资源
     * @param resourceList
     */
    void batchInsert(@Param("resourceList") List<Resource> resourceList);

    /**
     * 切换资源状态
     * @param resourceId
     * @param newStatus
     * @return
     */
    int changeStatus(@Param("resourceId") int resourceId, @Param("newStatus") int newStatus);
}
