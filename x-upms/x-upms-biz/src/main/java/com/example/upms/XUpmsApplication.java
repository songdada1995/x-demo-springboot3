package com.example.upms;

import com.example.common.core.config.AsyncConfig;
import com.example.common.core.config.JacksonConfig;
import com.example.common.core.exception.GlobalExceptionHandler;
import com.example.common.feign.annotation.EnableXFeignClients;
import com.example.common.mybatis.config.MybatisPlusConfig;
import com.example.common.mybatis.handler.MyMetaObjectHandler;
import com.example.common.redis.config.RedisConfig;
import com.example.common.security.aspect.PermissionAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import com.example.upms.api.aspect.LogAspect;
import com.example.upms.api.service.AsyncOperLogService;

@EnableXFeignClients(basePackages = "com.example")
@EnableDiscoveryClient
@Import({AsyncConfig.class, JacksonConfig.class, GlobalExceptionHandler.class, PermissionAspect.class,
        LogAspect.class, AsyncOperLogService.class, RedisConfig.class, MybatisPlusConfig.class, MyMetaObjectHandler.class})
@SpringBootApplication
public class XUpmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(XUpmsApplication.class, args);
    }
} 