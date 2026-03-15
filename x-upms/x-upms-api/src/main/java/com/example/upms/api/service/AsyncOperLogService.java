package com.example.upms.api.service;

import com.example.upms.api.domain.entity.SysOperLog;
import com.example.upms.api.feign.RemoteOperLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 操作日志异步服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncOperLogService {

    private final RemoteOperLogService remoteOperLogService;

    @Async("asyncExecutor")
    public void saveOperLogAsync(SysOperLog operLog) {
        try {
            remoteOperLogService.saveOperLog(operLog);
        } catch (Exception e) {
            log.error("操作日志异步保存失败: operName={}, title={}", operLog.getOperName(), operLog.getTitle(), e);
        }
    }
}
