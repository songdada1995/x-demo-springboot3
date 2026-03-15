package com.example.common.feign.annotation;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.lang.annotation.*;

/**
 * 组合注解：启用服务发现 + Feign 客户端
 * 适用于需要调用其他微服务的业务模块（如 upms、auth）
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableDiscoveryClient
@EnableXFeignClients
public @interface EnableXCloud {
}
