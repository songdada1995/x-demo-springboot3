package com.example.upms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.upms.api.domain.entity.SysOperLog;
import com.example.upms.mapper.SysOperLogMapper;
import com.example.upms.service.ISysOperLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 操作日志服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService {

    private final SysOperLogMapper operLogMapper;

    @Override
    @Async
    public void saveOperLog(SysOperLog operLog) {
        save(operLog);
        log.debug("保存操作日志：用户={}, 操作={}, 状态={}",
                 operLog.getOperName(), operLog.getTitle(), operLog.getStatus());
    }

    @Override
    public void cleanOperLog() {
        operLogMapper.cleanOperLog();
        log.info("清空操作日志成功");
    }
}
