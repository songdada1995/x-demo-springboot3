/*
 * Copyright (c) [song]. 2025. All rights reserved.
 */

package com.example.auth.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Instant;
import java.util.Map;

@Data
@TableName("oauth2_registered_client")
public class OAuth2RegisteredClient {
    private String id;
    private String clientId;
    private Instant clientIdIssuedAt;
    private String clientSecret;
    private Instant clientSecretExpiresAt;
    private String clientName;
    private String clientAuthenticationMethods;
    private String authorizationGrantTypes;
    private String redirectUris;
    private String scopes;
    private String clientSettings;
    private String tokenSettings;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static OAuth2RegisteredClient from(RegisteredClient registeredClient) {
        OAuth2RegisteredClient client = new OAuth2RegisteredClient();
        client.setId(registeredClient.getId());
        client.setClientId(registeredClient.getClientId());
        client.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        client.setClientSecret(registeredClient.getClientSecret());
        client.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
        client.setClientName(registeredClient.getClientName());
        client.setClientAuthenticationMethods(registeredClient.getClientAuthenticationMethods().stream()
                .map(ClientAuthenticationMethod::getValue)
                .reduce((a, b) -> a + "," + b)
                .orElse(""));
        client.setAuthorizationGrantTypes(registeredClient.getAuthorizationGrantTypes().stream()
                .map(AuthorizationGrantType::getValue)
                .reduce((a, b) -> a + "," + b)
                .orElse(""));
        client.setRedirectUris(registeredClient.getRedirectUris().stream()
                .reduce((a, b) -> a + "," + b)
                .orElse(""));
        client.setScopes(registeredClient.getScopes().stream()
                .reduce((a, b) -> a + "," + b)
                .orElse(""));
        try {
            client.setClientSettings(objectMapper.writeValueAsString(registeredClient.getClientSettings().getSettings()));
            client.setTokenSettings(objectMapper.writeValueAsString(registeredClient.getTokenSettings().getSettings()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize settings", e);
        }
        return client;
    }

    public RegisteredClient toRegisteredClient() {
        try {
            Map<String, Object> clientSettingsMap = objectMapper.readValue(clientSettings, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> tokenSettingsMap = objectMapper.readValue(tokenSettings, new TypeReference<Map<String, Object>>() {});

            return RegisteredClient.withId(this.id)
                    .clientId(this.clientId)
                    .clientIdIssuedAt(this.clientIdIssuedAt)
                    .clientSecret(this.clientSecret)
                    .clientSecretExpiresAt(this.clientSecretExpiresAt)
                    .clientName(this.clientName)
                    .clientAuthenticationMethods(methods -> {
                        for (String method : this.clientAuthenticationMethods.split(",")) {
                            methods.add(new ClientAuthenticationMethod(method));
                        }
                    })
                    .authorizationGrantTypes(types -> {
                        for (String type : this.authorizationGrantTypes.split(",")) {
                            types.add(new AuthorizationGrantType(type));
                        }
                    })
                    .redirectUris(uris -> {
                        for (String uri : this.redirectUris.split(",")) {
                            uris.add(uri);
                        }
                    })
                    .scopes(scopes -> {
                        for (String scope : this.scopes.split(",")) {
                            scopes.add(scope);
                        }
                    })
                    .clientSettings(ClientSettings.withSettings(clientSettingsMap).build())
                    .tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build())
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize settings", e);
        }
    }
} 