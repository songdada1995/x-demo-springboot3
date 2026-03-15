package com.example.upms.controller;

import com.example.common.core.response.R;
import com.example.common.core.utils.PageUtils;
import com.example.upms.api.domain.dto.SysRoleDTO;
import com.example.upms.api.domain.entity.SysMenu;
import com.example.upms.api.domain.vo.SysRoleVO;
import com.example.upms.mapper.SysMenuMapper;
import com.example.upms.service.ISysRoleMenuService;
import com.example.upms.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final ISysRoleService sysRoleService;
    private final ISysRoleMenuService roleMenuService;
    private final SysMenuMapper menuMapper;

    @GetMapping("/list")
    public R<PageUtils<SysRoleVO>> list(SysRoleDTO roleDTO) {
        return R.ok(sysRoleService.list(roleDTO));
    }

    @GetMapping("/{roleId}")
    public R<SysRoleVO> getInfo(@PathVariable("roleId") Long roleId) {
        return R.ok(sysRoleService.getInfo(roleId));
    }

    @PostMapping
    public R<Void> add(@RequestBody SysRoleDTO roleDTO) {
        sysRoleService.add(roleDTO);
        return R.ok();
    }

    @PutMapping
    public R<Void> edit(@RequestBody SysRoleDTO roleDTO) {
        sysRoleService.edit(roleDTO);
        return R.ok();
    }

    @DeleteMapping("/{roleId}")
    public R<Void> remove(@PathVariable("roleId") Long roleId) {
        sysRoleService.remove(roleId);
        return R.ok();
    }

    @GetMapping("/{roleId}/menus")
    public R<List<Long>> getRoleMenuIds(@PathVariable("roleId") Long roleId) {
        List<SysMenu> menus = menuMapper.selectMenusByRoleId(roleId);
        List<Long> menuIds = menus.stream().map(SysMenu::getMenuId).toList();
        return R.ok(menuIds);
    }

    @PutMapping("/{roleId}/menus")
    public R<Void> saveRoleMenus(@PathVariable("roleId") Long roleId, @RequestBody Map<String, List<Long>> body) {
        List<Long> menuIds = body.get("menuIds");
        roleMenuService.insertRoleMenu(roleId, menuIds != null ? menuIds : List.of());
        return R.ok();
    }
} 