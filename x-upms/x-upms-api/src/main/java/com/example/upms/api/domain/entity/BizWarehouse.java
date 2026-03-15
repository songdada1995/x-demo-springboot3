package com.example.upms.api.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("biz_warehouse")
public class BizWarehouse {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String warehouseName;
    private String warehouseType;
    private String region;
    private String address;
    private String manager;
    private String contactPhone;
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
