package com.example.auth;

import com.example.common.core.config.AsyncConfig;
import com.example.common.core.config.JacksonConfig;
import com.example.common.core.exception.GlobalExceptionHandler;
import com.example.common.feign.annotation.EnableXFeignClients;
import com.example.common.redis.config.RedisConfig;
import com.example.common.security.handler.CustomAccessDeniedHandler;
import com.example.common.security.handler.CustomAuthenticationEntryPoint;
import com.example.common.security.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@EnableXFeignClients(basePackages = "com.example")
@EnableDiscoveryClient
@Import({JwtUtils.class, AsyncConfig.class, JacksonConfig.class, GlobalExceptionHandler.class,
        CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class, RedisConfig.class})
@SpringBootApplication
public class XAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(XAuthApplication.class, args);
    }
} 