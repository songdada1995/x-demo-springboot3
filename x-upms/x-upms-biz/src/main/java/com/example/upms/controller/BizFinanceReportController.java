package com.example.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.core.response.R;
import com.example.common.core.utils.PageUtils;
import com.example.upms.api.domain.entity.BizFinanceReport;
import com.example.upms.mapper.BizFinanceReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/biz/report")
@RequiredArgsConstructor
public class BizFinanceReportController {

    private final BizFinanceReportMapper reportMapper;

    @GetMapping("/list")
    public R<PageUtils<BizFinanceReport>> list(
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "reportType", required = false) String reportType,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "remark", required = false) String remark) {

        Page<BizFinanceReport> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BizFinanceReport> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(reportType != null && !reportType.isEmpty(), BizFinanceReport::getReportType, reportType)
                .ge(startDate != null && !startDate.isEmpty(), BizFinanceReport::getReportDate, startDate)
                .le(endDate != null && !endDate.isEmpty(), BizFinanceReport::getReportDate, endDate)
                .like(remark != null && !remark.isEmpty(), BizFinanceReport::getRemark, remark)
                .orderByDesc(BizFinanceReport::getReportDate);

        reportMapper.selectPage(page, wrapper);
        return R.ok(new PageUtils<>(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent()));
    }

    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.with(TemporalAdjusters.firstDayOfMonth());

        BigDecimal income = sumByType("income", monthStart, now);
        BigDecimal expense = sumByType("expense", monthStart, now);
        BigDecimal profit = income.subtract(expense);

        Map<String, Object> result = new HashMap<>();
        result.put("monthIncome", income);
        result.put("monthExpense", expense);
        result.put("monthProfit", profit);
        return R.ok(result);
    }

    private BigDecimal sumByType(String type, LocalDate start, LocalDate end) {
        LambdaQueryWrapper<BizFinanceReport> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BizFinanceReport::getReportType, type)
                .ge(BizFinanceReport::getReportDate, start)
                .le(BizFinanceReport::getReportDate, end);
        List<BizFinanceReport> records = reportMapper.selectList(wrapper);
        return records.stream()
                .map(BizFinanceReport::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @GetMapping("/{id}")
    public R<BizFinanceReport> getInfo(@PathVariable("id") Long id) {
        return R.ok(reportMapper.selectById(id));
    }

    @PostMapping
    public R<Void> add(@RequestBody BizFinanceReport report) {
        reportMapper.insert(report);
        return R.ok();
    }

    @PutMapping
    public R<Void> edit(@RequestBody BizFinanceReport report) {
        reportMapper.updateById(report);
        return R.ok();
    }

    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable("ids") Long[] ids) {
        for (Long id : ids) {
            reportMapper.deleteById(id);
        }
        return R.ok();
    }
}
