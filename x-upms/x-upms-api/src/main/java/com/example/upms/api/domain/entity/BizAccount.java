package com.example.upms.api.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("biz_account")
public class BizAccount {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String accountName;
    private String accountType;
    private String contactPerson;
    private String contactPhone;
    private String email;
    private Integer status;
    private String description;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer delFlag;
}
