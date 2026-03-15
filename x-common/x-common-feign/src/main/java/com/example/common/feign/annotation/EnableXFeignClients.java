package com.example.common.feign.annotation;

import com.example.common.feign.config.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 自定义Feign客户端启用注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FeignAutoConfiguration.class)
@EnableFeignClients(basePackages = "com.example")
public @interface EnableXFeignClients {

    /**
     * Feign客户端扫描包路径
     */
    @AliasFor(annotation = EnableFeignClients.class, attribute = "basePackages")
    String[] value() default {"com.example"};

    /**
     * Feign客户端扫描包路径
     */
    @AliasFor(annotation = EnableFeignClients.class, attribute = "basePackages")
    String[] basePackages() default {"com.example"};

    /**
     * Feign客户端扫描类
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * Feign客户端默认配置类
     */
    Class<?>[] defaultConfiguration() default {};

    /**
     * Feign客户端类
     */
    Class<?>[] clients() default {};
} 