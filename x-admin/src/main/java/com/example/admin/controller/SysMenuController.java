package com.example.admin.controller;

import com.example.admin.domain.dto.SysMenuDTO;
import com.example.admin.domain.vo.SysMenuVO;
import com.example.admin.service.ISysMenuService;
import com.example.common.core.response.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜单管理")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final ISysMenuService menuService;

    @Operation(summary = "获取菜单列表")
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    public R<List<SysMenuVO>> list(SysMenuDTO menuDTO) {
        List<SysMenuVO> menus = menuService.list(menuDTO);
        return R.ok(menus);
    }

    @Operation(summary = "获取菜单详细信息")
    @GetMapping("/{menuId}")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    public R<SysMenuVO> getInfo(@PathVariable Long menuId) {
        return R.ok(menuService.getInfo(menuId));
    }

    @Operation(summary = "新增菜单")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    public R<Void> add(@Validated @RequestBody SysMenuDTO menuDTO) {
        menuService.add(menuDTO);
        return R.ok();
    }

    @Operation(summary = "修改菜单")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    public R<Void> edit(@Validated @RequestBody SysMenuDTO menuDTO) {
        menuService.edit(menuDTO);
        return R.ok();
    }

    @Operation(summary = "删除菜单")
    @DeleteMapping("/{menuId}")
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    public R<Void> remove(@PathVariable Long menuId) {
        menuService.remove(menuId);
        return R.ok();
    }

    @Operation(summary = "获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public R<List<SysMenuVO>> treeselect(SysMenuDTO menuDTO) {
        List<SysMenuVO> menus = menuService.list(menuDTO);
        return R.ok(menuService.buildMenuTree(menus));
    }

    @Operation(summary = "获取角色菜单列表")
    @GetMapping("/roleMenuTreeselect/{roleId}")
    public R<List<SysMenuVO>> roleMenuTreeselect(@PathVariable Long roleId) {
        List<SysMenuVO> menus = menuService.list(new SysMenuDTO());
        return R.ok(menuService.buildMenuTree(menus));
    }
} 