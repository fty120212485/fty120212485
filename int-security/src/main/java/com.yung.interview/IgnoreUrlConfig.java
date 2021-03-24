package com.yung.interview;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author: fengtianyong
 * @data: 2020/12/12 13:54
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "security.ignored")
public class IgnoreUrlConfig {
    List<String> urls = new ArrayList<>();
}
