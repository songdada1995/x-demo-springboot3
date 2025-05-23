package com.example.common.security.utils;

import com.example.common.core.exception.NoPermissionException;
import com.example.common.security.model.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

/**
 * 安全服务工具类
 */
public class SecurityUtils {

    /**
     * 获取用户
     */
    public static UserPrincipal getUser() {
        try {
            return (UserPrincipal) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new NoPermissionException("获取用户信息异常");
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        try {
            return getUser().getUserId();
        } catch (Exception e) {
            throw new NoPermissionException("获取用户ID异常");
        }
    }

    /**
     * 获取用户名
     */
    public static String getUsername() {
        try {
            return getUser().getUsername();
        } catch (Exception e) {
            throw new NoPermissionException("获取用户名异常");
        }
    }

    /**
     * 获取用户权限
     */
    public static Set<String> getPermissions() {
        try {
            return getUser().getPermissions();
        } catch (Exception e) {
            throw new NoPermissionException("获取用户权限异常");
        }
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
} 