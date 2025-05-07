package com.example.common.security.aspect;

import com.example.common.security.annotation.Logical;
import com.example.common.security.annotation.RequiresPermissions;
import com.example.common.core.exception.NoPermissionException;
import com.example.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

/**
 * 权限校验切面
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Before("@annotation(com.example.common.security.annotation.RequiresPermissions)")
    public void doBefore(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
        
        if (requiresPermissions != null) {
            String[] permissions = requiresPermissions.value();
            Logical logical = requiresPermissions.logical();
            
            // 获取当前用户权限
            Set<String> userPermissions = SecurityUtils.getPermissions();
            
            // 校验权限
            boolean hasPermission = false;
            if (logical == Logical.AND) {
                hasPermission = Arrays.stream(permissions)
                        .allMatch(userPermissions::contains);
            } else {
                hasPermission = Arrays.stream(permissions)
                        .anyMatch(userPermissions::contains);
            }
            
            if (!hasPermission) {
                log.warn("用户[{}]没有权限[{}]", SecurityUtils.getUsername(), Arrays.toString(permissions));
                throw new NoPermissionException("没有权限");
            }
        }
    }
} 