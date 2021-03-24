package com.yung.interview.utils;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2020/12/14 22:19
 */
public class SpringUtil {

    private static WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();

    public static Object getBean(String name){
        return context.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz){
        String name = clazz.getSimpleName();
        return context.getBean(name, clazz);
    }
}
