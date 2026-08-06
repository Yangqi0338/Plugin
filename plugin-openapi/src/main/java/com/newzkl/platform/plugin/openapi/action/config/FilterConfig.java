package com.newzkl.platform.plugin.openapi.action.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fang
 */
@Configuration
public class FilterConfig {

    @Autowired
    private HttpServletRequestReplacedFilter httpServletRequestReplacedFilter;
    @Autowired
    private SignatureFilter signatureFilter;

    @Bean
    public FilterRegistrationBean httpServletRequestReplacedFilterRegistration() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(httpServletRequestReplacedFilter);
        //拦截所有的请求，给每个请求都包装一下，拦截器中再判断是否需要拦截处理
        registrationBean.addUrlPatterns("/api/*");
        //给自定义的filter设置顺序，值越小，优先级越高，建议可以稍微高一些，防止影响框架的一些filter
        registrationBean.setOrder(10);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<SignatureFilter> signatureFilterRegistration() {
        FilterRegistrationBean<SignatureFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(signatureFilter);
        filter.addUrlPatterns("/api/*");
        filter.setName("signatureFilter");
        filter.setOrder(11);
        return filter;
    }
}