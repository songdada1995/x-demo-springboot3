package com.example.admin.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class SysUserDTO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 状态（0正常 1停用）
     */
    private Integer status;
    
    /**
     * 角色ID列表
     */
    private List<Long> roleIds;
    
    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;
} 