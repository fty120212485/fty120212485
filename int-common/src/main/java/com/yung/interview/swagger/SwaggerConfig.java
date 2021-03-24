package com.yung.interview.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2020/12/10 22:52
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Value("${swagger.enable}")
    private boolean isEnable;
    @Value("${jwt.header}")
    private String Administrator;

    @Bean
    public Docket docket(){
        List<RequestParameter> param = new ArrayList<>();
        param.add(new RequestParameterBuilder()
                .in(ParameterType.HEADER)
                .name(Administrator)
                .description("令牌")
                .required(false)
                .build());
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(isEnable)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yung.interview.controller"))
                //.paths(PathSelectors.none())
                .build()
                .globalRequestParameters(param);
    }

    private ApiInfo apiInfo(){
        return new ApiInfo(
                "Yung的接口文档",
                "生活就像海洋，只有意志坚强的人才能到达成功的彼岸！",
                "v1.0",
                "http://47.107.127.208:80/",
                new Contact("Yung","","120212485@qq.com"),
                "", "", new ArrayList<>()
        );
    }
}
