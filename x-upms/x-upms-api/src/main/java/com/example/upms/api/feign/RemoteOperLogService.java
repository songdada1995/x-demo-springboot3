package com.example.upms.api.feign;

import com.example.common.core.response.R;
import com.example.upms.api.domain.entity.SysOperLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 操作日志远程服务
 */
@FeignClient(value = "x-upms-biz", contextId = "remoteOperLogService")
public interface RemoteOperLogService {

    /**
     * 保存操作日志
     *
     * @param operLog 操作日志
     * @return 结果
     */
    @PostMapping("/api/system/operLog/save")
    R<Void> saveOperLog(@RequestBody SysOperLog operLog);
}
