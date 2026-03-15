package com.example.upms;

import com.example.common.feign.annotation.EnableXCloud;
import com.example.common.security.aspect.PermissionAspect;
import com.example.upms.api.aspect.LogAspect;
import com.example.upms.api.service.AsyncOperLogService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@EnableXCloud
@Import({LogAspect.class, AsyncOperLogService.class, PermissionAspect.class})
@SpringBootApplication
public class XUpmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(XUpmsApplication.class, args);
    }
} 