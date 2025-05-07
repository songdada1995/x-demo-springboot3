package com.example.auth.service.impl;

import com.example.auth.domain.dto.LoginDTO;
import com.example.auth.domain.vo.TokenVO;
import com.example.auth.service.AuthService;
import com.example.common.core.constant.Constants;
import com.example.common.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public TokenVO login(LoginDTO loginDTO) {
        // 认证
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成token
        String token = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());

        // 返回token信息
        TokenVO tokenVO = new TokenVO();
        tokenVO.setToken(token);
        tokenVO.setTokenType(Constants.TOKEN_PREFIX.trim());
        tokenVO.setExpiresIn(jwtUtils.getExpirationDateFromToken(token).getTime());

        return tokenVO;
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
} 