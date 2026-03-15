package com.example.upms.api.feign;

import com.example.common.core.response.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登录日志远程服务
 */
@FeignClient(value = "x-upms-biz", contextId = "remoteLoginLogService")
public interface RemoteLoginLogService {

    /**
     * 记录登录日志
     *
     * @param username  用户名
     * @param status    登录状态（0成功 1失败）
     * @param message   提示消息
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @return 结果
     */
    @PostMapping("/api/system/loginLog/record")
    R<Void> recordLoginLog(
            @RequestParam("username") String username,
            @RequestParam("status") String status,
            @RequestParam("message") String message,
            @RequestParam("ipAddress") String ipAddress,
            @RequestParam("userAgent") String userAgent
    );
}
