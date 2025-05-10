package com.example.auth;

import com.example.common.openfeign.annotation.EnableXFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableXFeignClients(basePackages = "com.example.auth.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class XAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(XAuthApplication.class, args);
    }
} 