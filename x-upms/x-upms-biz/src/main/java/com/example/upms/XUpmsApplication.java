package com.example.upms;

import com.example.common.openfeign.annotation.EnableXFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableXFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class XUpmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(XUpmsApplication.class, args);
    }
} 