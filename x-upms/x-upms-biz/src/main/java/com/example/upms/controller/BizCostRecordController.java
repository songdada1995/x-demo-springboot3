package com.example.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.core.response.R;
import com.example.common.core.utils.PageUtils;
import com.example.upms.api.domain.entity.BizCostRecord;
import com.example.upms.mapper.BizCostRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/biz/cost")
@RequiredArgsConstructor
public class BizCostRecordController {

    private final BizCostRecordMapper costMapper;

    @GetMapping("/list")
    public R<PageUtils<BizCostRecord>> list(
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "costType", required = false) String costType,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "remark", required = false) String remark) {

        Page<BizCostRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BizCostRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(costType != null && !costType.isEmpty(), BizCostRecord::getCostType, costType)
                .ge(startDate != null && !startDate.isEmpty(), BizCostRecord::getRecordDate, startDate)
                .le(endDate != null && !endDate.isEmpty(), BizCostRecord::getRecordDate, endDate)
                .like(remark != null && !remark.isEmpty(), BizCostRecord::getRemark, remark)
                .orderByDesc(BizCostRecord::getRecordDate);

        costMapper.selectPage(page, wrapper);
        return R.ok(new PageUtils<>(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent()));
    }

    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastMonthStart = monthStart.minusMonths(1);
        LocalDate lastMonthEnd = monthStart.minusDays(1);

        BigDecimal currentMonth = sumCost(monthStart, now);
        BigDecimal lastMonth = sumCost(lastMonthStart, lastMonthEnd);

        double growthRate = 0;
        if (lastMonth.compareTo(BigDecimal.ZERO) > 0) {
            growthRate = currentMonth.subtract(lastMonth)
                    .divide(lastMonth, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("currentMonthCost", currentMonth);
        result.put("lastMonthCost", lastMonth);
        result.put("growthRate", growthRate);
        return R.ok(result);
    }

    private BigDecimal sumCost(LocalDate start, LocalDate end) {
        LambdaQueryWrapper<BizCostRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(BizCostRecord::getRecordDate, start)
                .le(BizCostRecord::getRecordDate, end);
        return costMapper.selectList(wrapper).stream()
                .map(BizCostRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @GetMapping("/{id}")
    public R<BizCostRecord> getInfo(@PathVariable("id") Long id) {
        return R.ok(costMapper.selectById(id));
    }

    @PostMapping
    public R<Void> add(@RequestBody BizCostRecord record) {
        costMapper.insert(record);
        return R.ok();
    }

    @PutMapping
    public R<Void> edit(@RequestBody BizCostRecord record) {
        costMapper.updateById(record);
        return R.ok();
    }

    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable("ids") Long[] ids) {
        for (Long id : ids) {
            costMapper.deleteById(id);
        }
        return R.ok();
    }
}
