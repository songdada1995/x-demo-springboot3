package com.example.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.upms.api.domain.entity.SysOperLog;

/**
 * 操作日志服务接口
 */
public interface ISysOperLogService extends IService<SysOperLog> {

    /**
     * 保存操作日志
     *
     * @param operLog 操作日志
     */
    void saveOperLog(SysOperLog operLog);

    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
