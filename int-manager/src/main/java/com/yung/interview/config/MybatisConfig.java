package com.yung.interview.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2021/1/12 17:35
 */
@Configuration
@MapperScan(value = {"com.yung.interview.dao", "com.yung.interview.mapper"})
public class MybatisConfig {
}
