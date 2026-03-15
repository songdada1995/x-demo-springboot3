package com.example.gateway;

import com.example.common.security.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@EnableDiscoveryClient
@Import(JwtUtils.class)
@SpringBootApplication
public class XGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(XGatewayApplication.class, args);
    }
} 