package com.example.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.core.response.R;
import com.example.upms.api.domain.entity.SysLoginLog;
import com.example.upms.api.domain.entity.SysNotice;
import com.example.upms.api.domain.entity.SysUser;
import com.example.upms.mapper.SysLoginLogMapper;
import com.example.upms.mapper.SysNoticeMapper;
import com.example.upms.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final SysUserMapper userMapper;
    private final SysLoginLogMapper loginLogMapper;
    private final SysNoticeMapper noticeMapper;

    @GetMapping("/stats")
    public R<Map<String, Object>> stats() {
        Long userCount = userMapper.selectCount(null);

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LambdaQueryWrapper<SysLoginLog> logWrapper = new LambdaQueryWrapper<>();
        logWrapper.ge(SysLoginLog::getLoginTime, todayStart);
        Long todayVisits = loginLogMapper.selectCount(logWrapper);

        Long noticeCount = noticeMapper.selectCount(
                new LambdaQueryWrapper<SysNotice>().eq(SysNotice::getStatus, 0));

        Map<String, Object> result = new HashMap<>();
        result.put("userCount", userCount);
        result.put("todayVisits", todayVisits);
        result.put("messageCount", noticeCount);
        result.put("todoCount", 5);
        return R.ok(result);
    }

    @GetMapping("/notices")
    public R<List<SysNotice>> notices() {
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysNotice::getStatus, 0)
                .orderByDesc(SysNotice::getCreateTime)
                .last("LIMIT 5");
        return R.ok(noticeMapper.selectList(wrapper));
    }

    @GetMapping("/recentActivity")
    public R<List<Map<String, Object>>> recentActivity() {
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysLoginLog::getStatus, "0")
                .orderByDesc(SysLoginLog::getLoginTime)
                .last("LIMIT 5");
        List<SysLoginLog> logs = loginLogMapper.selectList(wrapper);

        List<Map<String, Object>> activities = new ArrayList<>();
        for (SysLoginLog log : logs) {
            Map<String, Object> item = new HashMap<>();
            item.put("content", log.getUsername() + " 登录系统");
            item.put("time", log.getLoginTime());
            activities.add(item);
        }
        return R.ok(activities);
    }
}
