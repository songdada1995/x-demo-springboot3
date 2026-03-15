package com.example.upms.controller;

import com.example.common.core.response.R;
import com.example.common.core.utils.PageUtils;
import com.example.common.security.model.LoginUser;
import com.example.upms.api.domain.dto.SysUserDTO;
import com.example.upms.api.domain.vo.SysUserVO;
import com.example.upms.security.UserPrincipal;
import com.example.upms.service.ISysMenuService;
import com.example.upms.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final ISysUserService sysUserService;
    private final ISysMenuService menuService;

    @GetMapping("/current")
    public R<Map<String, Object>> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            return R.fail("未登录");
        }
        Long userId = principal.getId();

        SysUserVO user = sysUserService.getInfo(userId);

        Set<String> permissions;
        if (userId == 1L) {
            permissions = Set.of("*:*:*");
        } else {
            permissions = menuService.selectPermsByUserId(userId);
        }

        List<Long> roleIds = sysUserService.getRoleIds(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("permissions", permissions);
        result.put("roleIds", roleIds);

        return R.ok(result);
    }

    @GetMapping("/list")
    public R<PageUtils<SysUserVO>> list(SysUserDTO userDTO) {
        return R.ok(sysUserService.list(userDTO));
    }

    @GetMapping("/{userId}")
    public R<SysUserVO> getInfo(@PathVariable("userId") Long userId) {
        return R.ok(sysUserService.getInfo(userId));
    }

    @PostMapping
    public R<Void> add(@RequestBody SysUserDTO userDTO) {
        sysUserService.add(userDTO);
        return R.ok();
    }

    @PutMapping
    public R<Void> edit(@RequestBody SysUserDTO userDTO) {
        sysUserService.edit(userDTO);
        return R.ok();
    }

    @DeleteMapping("/{userId}")
    public R<Void> remove(@PathVariable("userId") Long userId) {
        sysUserService.remove(userId);
        return R.ok();
    }

    @PutMapping("/resetPwd")
    public R<Void> resetPwd(@RequestBody SysUserDTO userDTO) {
        sysUserService.resetPwd(userDTO);
        return R.ok();
    }

    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestBody SysUserDTO userDTO) {
        sysUserService.changeStatus(userDTO);
        return R.ok();
    }

    @GetMapping("/role/{userId}")
    public R<List<Long>> getRoleIds(@PathVariable("userId") Long userId) {
        return R.ok(sysUserService.getRoleIds(userId));
    }

    @PutMapping("/role")
    public R<Void> assignRole(@RequestBody SysUserDTO userDTO) {
        sysUserService.assignRole(userDTO);
        return R.ok();
    }

    @GetMapping("/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username) {
        return R.ok(sysUserService.getUserInfo(username));
    }

    @PutMapping("/unlock/{username}")
    public R<Void> unlockAccount(@PathVariable("username") String username) {
        sysUserService.unlockAccount(username);
        return R.ok();
    }
} 