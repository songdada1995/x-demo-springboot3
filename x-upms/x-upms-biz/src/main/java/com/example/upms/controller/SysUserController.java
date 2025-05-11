package com.example.upms.controller;

import com.example.common.core.response.R;
import com.example.common.core.utils.PageUtils;
import com.example.common.security.annotation.RequiresPermissions;
import com.example.common.security.model.LoginUser;
import com.example.upms.api.domain.dto.SysUserDTO;
import com.example.upms.api.domain.vo.SysUserVO;
import com.example.upms.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final ISysUserService sysUserService;

    @GetMapping("/list")
    @RequiresPermissions("system:user:list")
    public R<PageUtils<SysUserVO>> list(SysUserDTO userDTO) {
        return R.ok(sysUserService.list(userDTO));
    }

    @GetMapping("/{userId}")
    @RequiresPermissions("system:user:query")
    public R<SysUserVO> getInfo(@PathVariable Long userId) {
        return R.ok(sysUserService.getInfo(userId));
    }

    @PostMapping
    @RequiresPermissions("system:user:add")
    public R<Void> add(@RequestBody SysUserDTO userDTO) {
        sysUserService.add(userDTO);
        return R.ok();
    }

    @PutMapping
    @RequiresPermissions("system:user:edit")
    public R<Void> edit(@RequestBody SysUserDTO userDTO) {
        sysUserService.edit(userDTO);
        return R.ok();
    }

    @DeleteMapping("/{userId}")
    @RequiresPermissions("system:user:remove")
    public R<Void> remove(@PathVariable Long userId) {
        sysUserService.remove(userId);
        return R.ok();
    }

    @PutMapping("/resetPwd")
    @RequiresPermissions("system:user:resetPwd")
    public R<Void> resetPwd(@RequestBody SysUserDTO userDTO) {
        sysUserService.resetPwd(userDTO);
        return R.ok();
    }

    @PutMapping("/changeStatus")
    @RequiresPermissions("system:user:edit")
    public R<Void> changeStatus(@RequestBody SysUserDTO userDTO) {
        sysUserService.changeStatus(userDTO);
        return R.ok();
    }

    @GetMapping("/role/{userId}")
    @RequiresPermissions("system:user:query")
    public R<List<Long>> getRoleIds(@PathVariable Long userId) {
        return R.ok(sysUserService.getRoleIds(userId));
    }

    @PutMapping("/role")
    @RequiresPermissions("system:user:edit")
    public R<Void> assignRole(@RequestBody SysUserDTO userDTO) {
        sysUserService.assignRole(userDTO);
        return R.ok();
    }

    /**
     * 获取用户信息和权限信息
     */
    @GetMapping("/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username) {
        return R.ok(sysUserService.getUserInfo(username));
    }
} 