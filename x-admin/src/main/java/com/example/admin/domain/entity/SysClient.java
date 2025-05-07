package com.example.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_client")
public class SysClient {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String clientId;
    private String clientSecret;
    private String clientName;
    private String redirectUri;
    private String grantTypes;
    private String scopes;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
    private Boolean deleted;
} 