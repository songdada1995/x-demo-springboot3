/*
 *
 *  * Copyright (c) [song]. 2025-2025. All rights reserved.
 *  * This software is provided 'as-is', without any express or implied warranty.
 *  * In no event will the authors be held liable for any damages arising from the use of this software.
 *  * Permission is granted to anyone to use this software for any purpose,
 *  * including commercial applications, and to alter it and redistribute it freely,
 *  * subject to the following restrictions:
 *  * 1. The origin of this software must not be misrepresented; you must not claim that you wrote the original software.
 *  *    If you use this software in a product, an acknowledgment in the product documentation would be appreciated but is not required.
 *  * 2. Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.
 *
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

import java.util.ArrayList;

/**
 * @author song
 * @date 2025/5/8 22:07
 */
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
