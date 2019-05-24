package com.example.demo.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private LoginInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    public LoginInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(LoginInterceptor interceptor) {
        this.interceptor = interceptor;
    }

}
