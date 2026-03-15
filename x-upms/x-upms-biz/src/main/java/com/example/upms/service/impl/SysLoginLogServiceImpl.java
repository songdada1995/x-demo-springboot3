package com.example.upms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.core.utils.IpLocationUtils;
import com.example.upms.api.domain.entity.SysLoginLog;
import com.example.upms.mapper.SysLoginLogMapper;
import com.example.upms.service.ISysLoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

import java.time.LocalDateTime;

/**
 * 登录日志服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {

    private final SysLoginLogMapper loginLogMapper;

    @Override
    @Async
    public void recordLoginLog(String username, String status, String message, String ipAddress, String userAgent) {
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setUsername(username);
        loginLog.setStatus(status);
        loginLog.setMsg(message);
        loginLog.setIpaddr(ipAddress);
        loginLog.setLoginLocation(IpLocationUtils.resolveLocation(ipAddress));

        // 解析User-Agent
        if (userAgent != null) {
            try {
                Parser uaParser = new Parser();
                Client client = uaParser.parse(userAgent);
                loginLog.setBrowser(client.userAgent.family + " " + client.userAgent.major);
                loginLog.setOs(client.os.family + " " + client.os.major);
            } catch (Exception e) {
                log.warn("Failed to parse user agent: {}", userAgent, e);
                loginLog.setBrowser("Unknown");
                loginLog.setOs("Unknown");
            }
        } else {
            loginLog.setBrowser("Unknown");
            loginLog.setOs("Unknown");
        }

        loginLog.setLoginTime(LocalDateTime.now());

        // 异步保存
        save(loginLog);
        log.debug("记录登录日志：用户={}, 状态={}, IP={}", username, status, ipAddress);
    }

    @Override
    public void cleanLoginLog() {
        loginLogMapper.cleanLoginLog();
        log.info("清空登录日志成功");
    }

}
