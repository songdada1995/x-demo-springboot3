package com.example.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.core.response.R;
import com.example.common.core.utils.PageUtils;
import com.example.upms.api.domain.entity.BizWarehouse;
import com.example.upms.mapper.BizWarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/biz/warehouse")
@RequiredArgsConstructor
public class BizWarehouseController {

    private final BizWarehouseMapper warehouseMapper;

    @GetMapping("/list")
    public R<PageUtils<BizWarehouse>> list(
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "warehouseName", required = false) String warehouseName,
            @RequestParam(name = "warehouseType", required = false) String warehouseType,
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "status", required = false) Integer status) {

        Page<BizWarehouse> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BizWarehouse> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(warehouseName != null && !warehouseName.isEmpty(), BizWarehouse::getWarehouseName, warehouseName)
                .eq(warehouseType != null && !warehouseType.isEmpty(), BizWarehouse::getWarehouseType, warehouseType)
                .eq(region != null && !region.isEmpty(), BizWarehouse::getRegion, region)
                .eq(status != null, BizWarehouse::getStatus, status)
                .orderByDesc(BizWarehouse::getCreateTime);

        warehouseMapper.selectPage(page, wrapper);
        return R.ok(new PageUtils<>(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent()));
    }

    @GetMapping("/{id}")
    public R<BizWarehouse> getInfo(@PathVariable("id") Long id) {
        return R.ok(warehouseMapper.selectById(id));
    }

    @PostMapping
    public R<Void> add(@RequestBody BizWarehouse warehouse) {
        warehouseMapper.insert(warehouse);
        return R.ok();
    }

    @PutMapping
    public R<Void> edit(@RequestBody BizWarehouse warehouse) {
        warehouseMapper.updateById(warehouse);
        return R.ok();
    }

    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable("ids") String ids) {
        if (ids != null && !ids.isEmpty()) {
            for (String s : ids.split(",")) {
                Long id = Long.parseLong(s.trim());
                warehouseMapper.deleteById(id);
            }
        }
        return R.ok();
    }
}
