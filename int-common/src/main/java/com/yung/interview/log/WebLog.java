package com.yung.interview.log;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2020/12/19 19:02
 */
@Getter
@Setter
public class WebLog {

    /**
     * 日志id
     */
    private Long id;
    /**
     * 操作描述
     */
    private String description;
    /**
     * 操作用户
     */
    private String username;
    /**
     * 操作人ip地址
     */
    private String ip;
    /**
     * 操作时间
     */
    private Date operationTime;
    /**
     * 执行结果
     */
    private Object result;
    /**
     * 请求参数
     */
    private String parameter;
    /**
     * 花费时间
     */
    private Integer spentTime;
    /**
     *  URL
     */
    private String url;
    /**
     *  URI
     */
    private String uri;
    /**
     * 请求类型
     */
    private String method;
}
