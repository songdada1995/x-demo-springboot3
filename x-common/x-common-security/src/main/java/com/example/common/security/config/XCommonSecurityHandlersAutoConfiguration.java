package com.example.common.security.config;

import com.example.common.security.handler.CustomAccessDeniedHandler;
import com.example.common.security.handler.CustomAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * x-common-security Servlet 安全处理器自动配置（仅 Auth 等 Servlet 应用）
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@Import({
        CustomAccessDeniedHandler.class,
        CustomAuthenticationEntryPoint.class
})
public class XCommonSecurityHandlersAutoConfiguration {
}
