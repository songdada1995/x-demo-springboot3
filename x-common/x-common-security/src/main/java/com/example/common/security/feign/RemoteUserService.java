package com.example.common.security.feign;

import com.example.common.core.response.R;
import com.example.common.security.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程用户服务
 */
@FeignClient(contextId = "remoteUserService", value = "x-admin")
public interface RemoteUserService {
    
    /**
     * 通过用户名查询用户信息和权限信息
     *
     * @param username 用户名
     * @return 用户信息和权限信息
     */
    @GetMapping("/user/info/{username}")
    R<LoginUser> getUserInfo(@PathVariable("username") String username);
} 