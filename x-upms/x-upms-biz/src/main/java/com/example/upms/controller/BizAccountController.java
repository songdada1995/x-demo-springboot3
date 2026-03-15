package com.example.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.core.response.R;
import com.example.common.core.utils.PageUtils;
import com.example.upms.api.domain.entity.BizAccount;
import com.example.upms.mapper.BizAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/biz/account")
@RequiredArgsConstructor
public class BizAccountController {

    private final BizAccountMapper accountMapper;

    @GetMapping("/list")
    public R<PageUtils<BizAccount>> list(
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "accountName", required = false) String accountName,
            @RequestParam(name = "accountType", required = false) String accountType,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "createTimeStart", required = false) String createTimeStart,
            @RequestParam(name = "createTimeEnd", required = false) String createTimeEnd) {

        Page<BizAccount> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BizAccount> wrapper = new LambdaQueryWrapper<>();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (createTimeStart != null && !createTimeStart.isEmpty()) {
            startTime = LocalDate.parse(createTimeStart).atStartOfDay();
        }
        if (createTimeEnd != null && !createTimeEnd.isEmpty()) {
            endTime = LocalDate.parse(createTimeEnd).atTime(23, 59, 59);
        }
        wrapper.like(accountName != null && !accountName.isEmpty(), BizAccount::getAccountName, accountName)
                .eq(accountType != null && !accountType.isEmpty(), BizAccount::getAccountType, accountType)
                .eq(status != null, BizAccount::getStatus, status)
                .ge(startTime != null, BizAccount::getCreateTime, startTime)
                .le(endTime != null, BizAccount::getCreateTime, endTime)
                .orderByDesc(BizAccount::getCreateTime);

        accountMapper.selectPage(page, wrapper);
        return R.ok(new PageUtils<>(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent()));
    }

    @GetMapping("/{id}")
    public R<BizAccount> getInfo(@PathVariable("id") Long id) {
        return R.ok(accountMapper.selectById(id));
    }

    @PostMapping
    public R<Void> add(@RequestBody BizAccount account) {
        accountMapper.insert(account);
        return R.ok();
    }

    @PutMapping
    public R<Void> edit(@RequestBody BizAccount account) {
        accountMapper.updateById(account);
        return R.ok();
    }

    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable("ids") String ids) {
        if (ids != null && !ids.isEmpty()) {
            for (String s : ids.split(",")) {
                Long id = Long.parseLong(s.trim());
                accountMapper.deleteById(id);
            }
        }
        return R.ok();
    }
}
