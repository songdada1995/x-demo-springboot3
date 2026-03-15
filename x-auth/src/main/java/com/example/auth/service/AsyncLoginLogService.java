package com.example.auth.service;

import com.example.upms.api.feign.RemoteLoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 登录日志异步服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncLoginLogService {

    private final RemoteLoginLogService remoteLoginLogService;

    @Async("asyncExecutor")
    public void recordLoginLogAsync(String username, String status, String message, String ipAddress, String userAgent) {
        try {
            remoteLoginLogService.recordLoginLog(username, status, message, ipAddress, userAgent);
        } catch (Exception e) {
            log.error("登录日志异步记录失败：username={}, status={}", username, status, e);
        }
    }
}
