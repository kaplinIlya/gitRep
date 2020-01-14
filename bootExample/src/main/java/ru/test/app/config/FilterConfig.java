package ru.test.app.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.test.app.filter.AuthorizationFilter;
import ru.test.app.filter.RequestResponseLoggingFilter;

@Configuration
public class FilterConfig {

    @Bean("requestResponseLoggingFilter")
    public FilterRegistrationBean<RequestResponseLoggingFilter> loggerFilter(){
        FilterRegistrationBean<RequestResponseLoggingFilter> registration = new FilterRegistrationBean();
        registration.addUrlPatterns("/printCar");
        registration.setFilter(new RequestResponseLoggingFilter());
        return registration;
    }

    @Bean("authorizationFilter")
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilter(){
        FilterRegistrationBean<AuthorizationFilter> registration = new FilterRegistrationBean();
        registration.addUrlPatterns("/printCar");
        registration.setFilter(new AuthorizationFilter());
        return registration;
    }
}
