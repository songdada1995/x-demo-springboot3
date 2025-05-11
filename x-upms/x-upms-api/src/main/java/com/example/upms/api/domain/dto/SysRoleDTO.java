package com.example.upms.api.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class SysRoleDTO {
    private Long roleId;
    private String roleName;
    private String roleKey;
    private Integer roleSort;
    private String dataScope;
    private Integer status;
    private String remark;
    private List<Long> menuIds;
    
    // 分页参数
    private Integer pageNum = 1;
    private Integer pageSize = 10;
} 