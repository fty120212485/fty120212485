package com.yung.interview.components;

import com.yung.interview.model.Resource;
import com.yung.interview.model.ResourceCategory;
import com.yung.interview.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * description: 资源信息初始化
 *
 * @author: fengtianyong
 * @data: 2021/1/14 21:49
 */
@Component
public class ResourceInit implements ApplicationRunner {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private ResourceService resourceService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        List<String> urls = new ArrayList();
        for (Resource resource : resourceService.listAll()) {
            urls.add(resource.getUrl());
        }
        List<Resource> resourceList = new ArrayList<>();
        handlerMethods.forEach((info, method) ->{
            Class<?> beanType = method.getBeanType();
            //封装资源分类信息
            Api annotation = beanType.getAnnotation(Api.class);
            int categoryId = 0;
            if(annotation != null){
                String ctrl = annotation.value();
                ResourceCategory resourceCategory = resourceService.findCategoryByCtrl(ctrl);
                if(resourceCategory == null){
                    resourceCategory = new ResourceCategory();
                    String tags = annotation.tags()[0];
                    resourceCategory.setController(ctrl);
                    resourceCategory.setName(tags);
                    resourceCategory.setSort(0);
                    resourceService.insertCategory(resourceCategory);
                }
                categoryId = resourceCategory.getId();
            }

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(getRequestUrl(beanType.getAnnotation(RequestMapping.class)));
            stringBuffer.append(getRequestUrl(method.getMethodAnnotation(RequestMapping.class)));
            String url = stringBuffer.toString();
                if (url.startsWith("/back") && !urls.contains(url)) {
                    Resource resource = new Resource();
                    ApiOperation apiOperation = method.getMethodAnnotation(ApiOperation.class);
                    String description = "";
                    if (apiOperation != null) {
                        description = apiOperation.value();
                    }
                    resource = new Resource();
                    resource.setUrl(url);
                    resource.setName("【" + description + "】");
                    resource.setStatus(Byte.parseByte(Integer.toString(1)));
                    resource.setCategoryId(categoryId);
                    resourceList.add(resource);
                }
        });
        if(resourceList.size() > 0){
            resourceService.batchInsert(resourceList);
        }
    }

    private StringBuffer getRequestUrl(RequestMapping requestMapping){
        StringBuffer url = new StringBuffer();
        for (String requestUrl : requestMapping.value()) {
            url.append(requestUrl);
        }
        return url;
    }
}
