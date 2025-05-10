package com.example.common.security.service;

import com.example.common.core.response.R;
import com.example.common.security.feign.RemoteUserService;
import com.example.common.security.model.LoginUser;
import org.springframework.stereotype.Service;

/**
 * 远程用户服务实现
 */
@Service
public class RemoteUserServiceImpl implements RemoteUserService {

    private final RemoteUserService remoteUserService;

    public RemoteUserServiceImpl(RemoteUserService remoteUserService) {
        this.remoteUserService = remoteUserService;
    }

    @Override
    public R<LoginUser> getUserInfo(String username) {
        return remoteUserService.getUserInfo(username);
    }
} 