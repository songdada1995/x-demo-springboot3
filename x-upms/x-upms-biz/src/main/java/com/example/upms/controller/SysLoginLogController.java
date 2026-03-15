package com.example.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.core.annotation.Log;
import com.example.common.core.response.R;
import com.example.common.core.utils.PageUtils;
import com.example.common.security.annotation.RequiresPermissions;
import com.example.upms.api.domain.entity.SysLoginLog;
import com.example.common.core.enums.BusinessType;
import com.example.upms.service.ISysLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 登录日志Controller
 */
@RestController
@RequestMapping("/api/system/loginLog")
@RequiredArgsConstructor
public class SysLoginLogController {

    private final ISysLoginLogService loginLogService;

    /**
     * 记录登录日志（供内部服务调用）
     */
    @PostMapping("/record")
    public R<Void> recordLoginLog(
            @RequestParam("username") String username,
            @RequestParam("status") String status,
            @RequestParam("message") String message,
            @RequestParam("ipAddress") String ipAddress,
            @RequestParam("userAgent") String userAgent) {

        loginLogService.recordLoginLog(username, status, message, ipAddress, userAgent);
        return R.ok();
    }

    /**
     * 查询登录日志列表
     */
    @GetMapping("/list")
    @RequiresPermissions("monitor:loginLog:list")
    @Log(title = "登录日志", businessType = BusinessType.SELECT)
    public R<PageUtils<SysLoginLog>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "ipaddr", required = false) String ipaddr,
            @RequestParam(value = "status", required = false) String status) {

        Page<SysLoginLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(username)) {
            wrapper.like(SysLoginLog::getUsername, username);
        }
        if (StringUtils.hasText(ipaddr)) {
            wrapper.like(SysLoginLog::getIpaddr, ipaddr);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(SysLoginLog::getStatus, status);
        }

        wrapper.orderByDesc(SysLoginLog::getLoginTime);

        Page<SysLoginLog> resultPage = loginLogService.page(page, wrapper);

        PageUtils<SysLoginLog> pageUtils = new PageUtils<>(
                resultPage.getRecords(), resultPage.getTotal(),
                resultPage.getSize(), resultPage.getCurrent());

        return R.ok(pageUtils);
    }

    /**
     * 清空登录日志
     */
    @DeleteMapping("/clean")
    @RequiresPermissions("monitor:loginLog:remove")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    public R<Void> clean() {
        loginLogService.cleanLoginLog();
        return R.ok();
    }

    /**
     * 删除登录日志
     */
    @DeleteMapping("/{infoIds}")
    @RequiresPermissions("monitor:loginLog:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    public R<Void> remove(@PathVariable("infoIds") Long[] infoIds) {
        for (Long infoId : infoIds) {
            loginLogService.removeById(infoId);
        }
        return R.ok();
    }
}
