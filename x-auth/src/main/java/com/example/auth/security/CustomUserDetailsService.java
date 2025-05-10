/*
 * Copyright (c) [song]. 2025. All rights reserved.
 */

package com.example.auth.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auth.domain.entity.SysUser;
import com.example.auth.mapper.SysUserMapper;
import com.example.common.security.model.UserPrincipal;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            List<String> roles = new ArrayList<>();
            List<GrantedAuthority> authorities = roles.stream()
                    .map(permission -> new SimpleGrantedAuthority(permission))
                    .collect(Collectors.toList());
            return UserPrincipal.builder()
                    .userId(sysUser.getUserId())
                    .username(sysUser.getUsername())
                    .password(sysUser.getPassword())
                    .status(sysUser.getStatus())
                    .accountLocked(sysUser.getAccountLocked())
                    .authorities(authorities)
                    .build();
        }
    }
}
