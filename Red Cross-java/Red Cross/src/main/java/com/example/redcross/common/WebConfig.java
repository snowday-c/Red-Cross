package com.example.redcross.common;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class WebConfig implements  WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 指定controller统一的接口前缀
        configurer.addPathPrefix("/api", clazz -> clazz.isAnnotationPresent(RestController.class));
    }

    @Resource
    private JwtInterceptor jwtInterceptor;

    // 加自定义拦截器JwtInterceptor，设置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/login")
                .excludePathPatterns("/api/user/admin")
                .excludePathPatterns("/api/user/register")
                .excludePathPatterns("/api/user/email/sendCode")
                .excludePathPatterns("/api/user/email/verifyCode")
                .excludePathPatterns("/api/user/forget/password")
                .excludePathPatterns("/api/user/update/password")
                .excludePathPatterns("/api/train/historyTrain");

    }

}