package com.example.auth;

import com.example.common.feign.annotation.EnableXCloud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableXCloud
@SpringBootApplication
public class XAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(XAuthApplication.class, args);
    }
} 