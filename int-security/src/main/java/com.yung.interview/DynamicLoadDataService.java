package com.yung.interview;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2021/1/15 20:10
 */
public interface DynamicLoadDataService {

    Map<String, ConfigAttribute> loadDataSource();
}
