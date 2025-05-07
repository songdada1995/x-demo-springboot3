package com.example.common.core.constant;

public interface Constants {
    /**
     * 成功标记
     */
    Integer SUCCESS = 200;
    
    /**
     * 失败标记
     */
    Integer ERROR = 500;
    
    /**
     * 未认证
     */
    Integer UNAUTHORIZED = 401;
    
    /**
     * 未授权
     */
    Integer FORBIDDEN = 403;
    
    /**
     * 请求头中的token
     */
    String TOKEN_HEADER = "Authorization";
    
    /**
     * token前缀
     */
    String TOKEN_PREFIX = "Bearer ";
    
    /**
     * token过期时间（分钟）
     */
    long TOKEN_EXPIRE = 60;
    
    /**
     * 刷新token过期时间（天）
     */
    long REFRESH_TOKEN_EXPIRE = 7;
    
    /**
     * 用户ID字段
     */
    String USER_ID = "userId";
    
    /**
     * 用户名字段
     */
    String USERNAME = "username";
} 