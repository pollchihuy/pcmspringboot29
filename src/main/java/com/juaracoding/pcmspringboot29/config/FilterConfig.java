package com.juaracoding.pcmspringboot29.config;

import com.juaracoding.pcmspringboot29.security.CustomHttpFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CustomHttpFilter> customHttpFilter() {
        FilterRegistrationBean<CustomHttpFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new CustomHttpFilter());
        registrationBean.addUrlPatterns("/*"); // Atur pola URL untuk disaring
        registrationBean.setOrder(1); // Atur urutan filter jika ada beberapa

        return registrationBean;
    }
}
