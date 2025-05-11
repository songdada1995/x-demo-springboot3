package com.example.auth;

import com.example.common.feign.annotation.EnableXFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableXFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class XAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(XAuthApplication.class, args);
    }
} 