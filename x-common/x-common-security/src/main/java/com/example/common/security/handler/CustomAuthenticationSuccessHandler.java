package com.example.common.security.handler;

import com.example.common.core.response.R;
import com.example.common.security.model.UserPrincipal;
import com.example.common.security.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userPrincipal);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("username", userPrincipal.getUsername());

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(R.ok(data)));
    }
} 