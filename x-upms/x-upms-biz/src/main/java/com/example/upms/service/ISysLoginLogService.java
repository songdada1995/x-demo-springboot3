package com.example.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.upms.api.domain.entity.SysLoginLog;

/**
 * 登录日志服务接口
 */
public interface ISysLoginLogService extends IService<SysLoginLog> {

    /**
     * 记录登录日志
     *
     * @param username      用户名
     * @param status        登录状态（0成功 1失败）
     * @param message       提示消息
     * @param ipAddress     IP地址
     * @param userAgent     用户代理信息
     */
    void recordLoginLog(String username, String status, String message, String ipAddress, String userAgent);

    /**
     * 清空登录日志
     */
    void cleanLoginLog();
}
