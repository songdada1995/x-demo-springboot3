package com.example.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.core.annotation.Log;
import com.example.common.core.response.R;
import com.example.common.core.utils.PageUtils;
import com.example.common.security.annotation.RequiresPermissions;
import com.example.upms.api.domain.entity.SysOperLog;
import com.example.common.core.enums.BusinessType;
import com.example.upms.service.ISysOperLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志Controller
 */
@RestController
@RequestMapping("/api/system/operLog")
@RequiredArgsConstructor
public class SysOperLogController {

    private final ISysOperLogService operLogService;

    /**
     * 保存操作日志（供内部服务调用）
     */
    @PostMapping("/save")
    public R<Void> saveOperLog(@RequestBody SysOperLog operLog) {
        operLogService.saveOperLog(operLog);
        return R.ok();
    }

    /**
     * 查询操作日志列表
     */
    @GetMapping("/list")
    @RequiresPermissions("monitor:operLog:list")
    @Log(title = "操作日志", businessType = BusinessType.SELECT)
    public R<PageUtils<SysOperLog>> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "operName", required = false) String operName,
            @RequestParam(value = "businessType", required = false) Integer businessType,
            @RequestParam(value = "status", required = false) Integer status) {

        Page<SysOperLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(title)) {
            wrapper.like(SysOperLog::getTitle, title);
        }
        if (StringUtils.hasText(operName)) {
            wrapper.like(SysOperLog::getOperName, operName);
        }
        if (businessType != null) {
            wrapper.eq(SysOperLog::getBusinessType, businessType);
        }
        if (status != null) {
            wrapper.eq(SysOperLog::getStatus, status);
        }

        wrapper.orderByDesc(SysOperLog::getOperTime);

        Page<SysOperLog> resultPage = operLogService.page(page, wrapper);

        PageUtils<SysOperLog> pageUtils = new PageUtils<>(
                resultPage.getRecords(), resultPage.getTotal(),
                resultPage.getSize(), resultPage.getCurrent());

        return R.ok(pageUtils);
    }

    /**
     * 获取操作日志详情
     */
    @GetMapping("/{operId}")
    @RequiresPermissions("monitor:operLog:query")
    public R<SysOperLog> getInfo(@PathVariable("operId") Long operId) {
        return R.ok(operLogService.getById(operId));
    }

    /**
     * 清空操作日志
     */
    @DeleteMapping("/clean")
    @RequiresPermissions("monitor:operLog:remove")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    public R<Void> clean() {
        operLogService.cleanOperLog();
        return R.ok();
    }

    /**
     * 删除操作日志
     */
    @DeleteMapping("/{operIds}")
    @RequiresPermissions("monitor:operLog:remove")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    public R<Void> remove(@PathVariable("operIds") Long[] operIds) {
        for (Long operId : operIds) {
            operLogService.removeById(operId);
        }
        return R.ok();
    }
}
