package com.example.common.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * OAuth2 授权服务器自动配置
 * 当存在 RegisteredClientRepository 时加载（如 x-auth 模块）
 */
@Configuration
@ConditionalOnBean(RegisteredClientRepository.class)
@Import(OAuth2AuthorizationServerConfig.class)
public class OAuth2AuthorizationServerAutoConfiguration {
}
