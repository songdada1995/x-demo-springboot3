package com.example.auth.service;

import com.example.auth.domain.dto.LoginDTO;
import com.example.auth.domain.vo.TokenVO;

public interface AuthService {
    /**
     * 登录
     */
    TokenVO login(LoginDTO loginDTO);

    /**
     * 登出
     */
    void logout();
} 