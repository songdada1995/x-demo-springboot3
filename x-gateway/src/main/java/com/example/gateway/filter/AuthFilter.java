package com.example.gateway.filter;

import com.example.common.core.response.R;
import com.example.common.security.utils.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 网关JWT认证过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 白名单路径 - 这些路径不需要JWT认证
     */
    private static final List<String> WHITE_LIST = List.of(
            "/auth/login",
            "/auth/logout",
            "/auth/register",
            "/oauth2/**",
            "/.well-known/**",
            "/actuator/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 检查是否在白名单中
        if (isWhitePath(path)) {
            log.debug("Path {} is in whitelist, skip auth", path);
            return chain.filter(exchange);
        }

        // 获取Authorization头
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 检查token是否存在
        if (!StringUtils.hasText(authHeader)) {
            log.warn("Missing Authorization header for path: {}", path);
            return unauthorized(exchange.getResponse(), "未提供认证令牌");
        }

        // 检查token格式
        if (!authHeader.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header format for path: {}", path);
            return unauthorized(exchange.getResponse(), "认证令牌格式错误");
        }

        // 提取JWT token
        String token = authHeader.substring(7);

        // 验证token
        try {
            if (!jwtUtils.validateToken(token)) {
                log.warn("Invalid JWT token for path: {}", path);
                return unauthorized(exchange.getResponse(), "认证令牌无效或已过期");
            }

            // 从token中提取用户信息
            String username = jwtUtils.getUsernameFromToken(token);

            // 将用户信息添加到请求头，传递给下游服务
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Name", username)
                    .build();

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();

            log.debug("User {} authenticated successfully for path: {}", username, path);
            return chain.filter(modifiedExchange);

        } catch (Exception e) {
            log.error("JWT token validation failed for path: {}", path, e);
            return unauthorized(exchange.getResponse(), "认证令牌验证失败");
        }
    }

    /**
     * 检查路径是否在白名单中
     */
    private boolean isWhitePath(String path) {
        return WHITE_LIST.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    /**
     * 返回未授权响应
     */
    private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        R<Void> result = R.fail(HttpStatus.UNAUTHORIZED.value(), message);

        try {
            byte[] bytes = objectMapper.writeValueAsString(result).getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Failed to write unauthorized response", e);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        // 设置较高优先级，在限流等过滤器之后执行
        return -100;
    }
}
