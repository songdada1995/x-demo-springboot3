package com.example.auth.config;

import com.example.common.core.response.R;
import com.example.upms.api.domain.entity.OAuth2RegisteredClient;
import com.example.upms.api.feign.RemoteClientDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * OAuth2 客户端注册配置
 * 从 UPMS 数据库加载客户端信息，供 x-common-security 的 OAuth2AuthorizationServerConfig 使用
 */
@Configuration
public class OAuth2ClientRegistrationConfig {

    private final RemoteClientDetailsService remoteClientDetailsService;

    public OAuth2ClientRegistrationConfig(RemoteClientDetailsService remoteClientDetailsService) {
        this.remoteClientDetailsService = remoteClientDetailsService;
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new RegisteredClientRepository() {
            @Override
            public void save(RegisteredClient registeredClient) {
                OAuth2RegisteredClient client = OAuth2RegisteredClient.from(registeredClient);
                remoteClientDetailsService.insert(client);
            }

            @Override
            public RegisteredClient findById(String id) {
                R<OAuth2RegisteredClient> r = remoteClientDetailsService.selectById(id);
                OAuth2RegisteredClient client = r != null ? r.getData() : null;
                return client != null ? client.toRegisteredClient() : null;
            }

            @Override
            public RegisteredClient findByClientId(String clientId) {
                R<OAuth2RegisteredClient> r = remoteClientDetailsService.getClientDetailsById(clientId);
                OAuth2RegisteredClient client = r.getData();
                return client != null ? client.toRegisteredClient() : null;
            }
        };
    }
}
