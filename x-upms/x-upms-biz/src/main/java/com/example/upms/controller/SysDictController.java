package com.example.upms.controller;

import com.example.common.core.response.R;
import com.example.upms.api.domain.entity.SysDict;
import com.example.upms.service.ISysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "字典管理")
@RestController
@RequestMapping("/api/system/dict")
public class SysDictController {

    @Autowired
    private ISysDictService dictService;

    @Operation(summary = "添加字典")
    @PostMapping
    @PreAuthorize("hasAuthority('sys:dict:add')")
    public R<Void> addDict(@RequestBody SysDict dict) {
        dictService.addDict(dict);
        return R.ok();
    }

    @Operation(summary = "更新字典")
    @PutMapping
    @PreAuthorize("hasAuthority('sys:dict:update')")
    public R<Void> updateDict(@RequestBody SysDict dict) {
        dictService.updateDict(dict);
        return R.ok();
    }

    @Operation(summary = "删除字典")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    public R<Void> deleteDict(@PathVariable Long id) {
        dictService.deleteDict(id);
        return R.ok();
    }
} 