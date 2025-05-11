package com.example.common.feign.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign自动配置
 */
@Configuration
public class FeignAutoConfiguration {

    /**
     * Feign日志级别
     */
    @Bean
    @ConditionalOnMissingBean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Feign请求拦截器
     */
    @Bean
    @ConditionalOnMissingBean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // 可以在这里添加通用的请求头
            requestTemplate.header("Content-Type", "application/json;charset=UTF-8");
        };
    }
} 