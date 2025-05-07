package com.example.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("sys_menu")
public class SysMenu {
    
    @TableId(type = IdType.AUTO)
    private Long menuId;
    
    private String menuName;
    
    private Long parentId;
    
    private Integer orderNum;
    
    private String path;
    
    private String component;
    
    private String query;
    
    private String isFrame;
    
    private String isCache;
    
    private String menuType;
    
    private String visible;
    
    private String status;
    
    private String perms;
    
    private String icon;
    
    private String remark;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    
    @TableLogic
    private Integer delFlag;
    
    @TableField(exist = false)
    private List<SysMenu> children;
} 