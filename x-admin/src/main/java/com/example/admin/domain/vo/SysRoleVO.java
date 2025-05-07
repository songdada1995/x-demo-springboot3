package com.example.admin.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class SysRoleVO {
    private Long roleId;
    private String roleName;
    private String roleKey;
    private Integer roleSort;
    private String dataScope;
    private Integer status;
    private String remark;
    private List<Long> menuIds;
} 