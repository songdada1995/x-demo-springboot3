package com.example.common.security.config;

import com.example.common.security.utils.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * x-common-security JWT 自动配置（Gateway、Auth 均需）
 */
@Configuration
@ConditionalOnClass(Jwts.class)
@Import(JwtUtils.class)
public class XCommonSecurityJwtAutoConfiguration {
}
