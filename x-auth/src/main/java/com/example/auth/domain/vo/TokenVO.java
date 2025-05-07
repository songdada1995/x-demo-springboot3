package com.example.auth.domain.vo;

import lombok.Data;

@Data
public class TokenVO {
    private String token;
    private String tokenType;
    private Long expiresIn;
} 