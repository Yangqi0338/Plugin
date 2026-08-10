package com.newzkl.platform.plugin.hdh.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fang
 */
@Configuration
public class HdhFilterConfig {
    
    @Autowired
    private HdhCallBackFilter hdhCallBackFilter;

    @Bean
    public FilterRegistrationBean<HdhCallBackFilter> hdhCallBackFilterRegistration() {
        FilterRegistrationBean<HdhCallBackFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(hdhCallBackFilter);
        filter.addUrlPatterns("/hdh/notify/*");
        filter.setName("hdhCallBackFilter");
        filter.setOrder(12);
        return filter;
    }
}