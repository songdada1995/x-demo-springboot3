/*
 * Copyright (c) [song]. 2025. All rights reserved.
 */

package com.example.auth.security;

import com.example.common.core.response.R;
import com.example.common.security.model.LoginUser;
import com.example.common.security.model.UserPrincipal;
import com.example.upms.api.feign.RemoteUserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author song
 * @date 2025/5/8 22:07
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private RemoteUserService remoteUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        R<LoginUser> userInfo = remoteUserService.getUserInfo(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException(username);
        } else {
            LoginUser loginUser = userInfo.getData();
            List<GrantedAuthority> authorities = loginUser.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
            return UserPrincipal.builder()
                    .userId(loginUser.getUserId())
                    .username(loginUser.getUsername())
                    .password(loginUser.getPassword())
                    .status(loginUser.getStatus())
                    .accountLocked(loginUser.getAccountLocked())
                    .authorities(authorities)
                    .build();
        }
    }
}
