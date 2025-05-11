package com.example.upms.security;

import com.example.upms.api.domain.entity.SysUser;
import com.example.upms.service.ISysMenuService;
import com.example.upms.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

/**
 * JWT用户详情服务
 */
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final ISysUserService userService;
    private final ISysMenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        Set<String> permissions = menuService.selectPermsByUserId(user.getUserId());
        return UserPrincipal.create(user, new ArrayList<>(permissions));
    }
} 