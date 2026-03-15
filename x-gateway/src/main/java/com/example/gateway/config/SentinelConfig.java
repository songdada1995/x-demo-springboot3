package com.example.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.example.common.core.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import jakarta.annotation.PostConstruct;
import java.util.*;

/**
 * Sentinel网关限流配置
 */
@Slf4j
@Configuration
public class SentinelConfig {

    /**
     * 配置限流规则
     */
    @PostConstruct
    public void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();

        // 认证服务限流规则 - 登录接口每秒最多10次请求
        rules.add(new GatewayFlowRule("x-auth")
                .setCount(100)  // QPS阈值
                .setIntervalSec(1));  // 统计时间窗口，单位秒

        // 用户管理服务限流规则 - 每秒最多50次请求
        rules.add(new GatewayFlowRule("x-upms-biz")
                .setCount(200)
                .setIntervalSec(1));

        // 加载规则
        GatewayRuleManager.loadRules(rules);

        log.info("Sentinel gateway flow rules loaded successfully");
        log.info("Auth service limit: 100 QPS");
        log.info("UPMS service limit: 200 QPS");
    }

    /**
     * 自定义限流响应
     */
    @PostConstruct
    public void initBlockHandler() {
        BlockRequestHandler blockRequestHandler = (exchange, t) -> {
            R<Void> result = R.fail(HttpStatus.TOO_MANY_REQUESTS.value(),
                    "请求过于频繁，请稍后重试");

            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(result));
        };

        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
        log.info("Sentinel block handler configured");
    }
}
