package com.example.common.security.interceptor;

import com.example.common.security.annotation.Logical;
import com.example.common.security.annotation.RequiresPermissions;
import com.example.common.core.exception.BusinessException;
import com.example.common.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

/**
 * 权限拦截器
 */
@Component
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequiresPermissions requiresPermissions = handlerMethod.getMethodAnnotation(RequiresPermissions.class);
        if (requiresPermissions == null) {
            return true;
        }

        String[] permissions = requiresPermissions.value();
        if (permissions.length == 0) {
            return true;
        }

        Set<String> userPermissions = SecurityUtils.getLoginUser().getPermissions();
        if (requiresPermissions.logical() == Logical.AND) {
            for (String permission : permissions) {
                if (!userPermissions.contains(permission)) {
                    throw new BusinessException("没有访问权限");
                }
            }
        } else {
            boolean hasPermission = false;
            for (String permission : permissions) {
                if (userPermissions.contains(permission)) {
                    hasPermission = true;
                    break;
                }
            }
            if (!hasPermission) {
                throw new BusinessException("没有访问权限");
            }
        }

        return true;
    }
} 