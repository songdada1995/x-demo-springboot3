package com.example.upms.controller;

import com.example.common.core.response.R;
import com.example.common.core.utils.PageUtils;
import com.example.upms.api.domain.dto.SysRoleDTO;
import com.example.upms.api.domain.vo.SysRoleVO;
import com.example.upms.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final ISysRoleService sysRoleService;

    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    public R<PageUtils<SysRoleVO>> list(SysRoleDTO roleDTO) {
        return R.ok(sysRoleService.list(roleDTO));
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    public R<SysRoleVO> getInfo(@PathVariable Long roleId) {
        return R.ok(sysRoleService.getInfo(roleId));
    }

    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    public R<Void> add(@RequestBody SysRoleDTO roleDTO) {
        sysRoleService.add(roleDTO);
        return R.ok();
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    public R<Void> edit(@RequestBody SysRoleDTO roleDTO) {
        sysRoleService.edit(roleDTO);
        return R.ok();
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    public R<Void> remove(@PathVariable Long roleId) {
        sysRoleService.remove(roleId);
        return R.ok();
    }
} 