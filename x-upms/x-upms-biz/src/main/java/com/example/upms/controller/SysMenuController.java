package com.example.upms.controller;

import com.example.common.core.response.R;
import com.example.upms.api.domain.dto.SysMenuDTO;
import com.example.upms.api.domain.vo.SysMenuVO;
import com.example.upms.security.UserPrincipal;
import com.example.upms.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/system/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final ISysMenuService menuService;

    @GetMapping("/list")
    public R<List<SysMenuVO>> list(SysMenuDTO menuDTO) {
        List<SysMenuVO> menus = menuService.list(menuDTO);
        return R.ok(menus);
    }

    @GetMapping("/user/routes")
    public R<List<SysMenuVO>> getUserRoutes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            return R.fail("未登录");
        }
        Long userId = principal.getId();

        List<SysMenuVO> menus;
        if (userId == 1L) {
            menus = menuService.list(new SysMenuDTO()).stream()
                    .filter(m -> "0".equals(m.getStatus()))
                    .collect(Collectors.toList());
        } else {
            menus = menuService.selectMenusByUserId(userId);
        }

        List<SysMenuVO> navigationMenus = menus.stream()
                .filter(m -> "M".equals(m.getMenuType()) || "C".equals(m.getMenuType()))
                .collect(Collectors.toList());

        return R.ok(menuService.buildMenuTree(navigationMenus));
    }

    @GetMapping("/{menuId}")
    public R<SysMenuVO> getInfo(@PathVariable("menuId") Long menuId) {
        return R.ok(menuService.getInfo(menuId));
    }

    @PostMapping
    public R<Void> add(@Validated @RequestBody SysMenuDTO menuDTO) {
        menuService.add(menuDTO);
        return R.ok();
    }

    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysMenuDTO menuDTO) {
        menuService.edit(menuDTO);
        return R.ok();
    }

    @DeleteMapping("/{menuId}")
    public R<Void> remove(@PathVariable("menuId") Long menuId) {
        menuService.remove(menuId);
        return R.ok();
    }

    @GetMapping("/treeselect")
    public R<List<SysMenuVO>> treeselect(SysMenuDTO menuDTO) {
        List<SysMenuVO> menus = menuService.list(menuDTO);
        return R.ok(menuService.buildMenuTree(menus));
    }

    @GetMapping("/roleMenuTreeselect/{roleId}")
    public R<List<SysMenuVO>> roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysMenuVO> menus = menuService.list(new SysMenuDTO());
        return R.ok(menuService.buildMenuTree(menus));
    }
} 