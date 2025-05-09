/*
 * Copyright (c) [song]. 2025. All rights reserved.
 */

package com.example.auth.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auth.domain.entity.SysUser;
import com.example.auth.domain.security.UserPrincipal;
import com.example.auth.mapper.SysUserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author song
 * @date 2025/5/8 22:07
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        if (sysUser == null) {
            throw new UsernameNotFoundException(username);
        } else {
            return UserPrincipal.create(sysUser, new ArrayList<>());
        }
    }
}
