package com.example.upms.api.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 菜单Excel实体
 */
@Data
public class SysMenuExcel {
    /**
     * 菜单ID
     */
    @ExcelProperty("菜单ID")
    @ColumnWidth(10)
    private Long menuId;

    /**
     * 菜单名称
     */
    @ExcelProperty("菜单名称")
    @ColumnWidth(20)
    private String menuName;

    /**
     * 父菜单ID
     */
    @ExcelProperty("父菜单ID")
    @ColumnWidth(10)
    private Long parentId;

    /**
     * 显示顺序
     */
    @ExcelProperty("显示顺序")
    @ColumnWidth(10)
    private Integer orderNum;

    /**
     * 路由地址
     */
    @ExcelProperty("路由地址")
    @ColumnWidth(30)
    private String path;

    /**
     * 组件路径
     */
    @ExcelProperty("组件路径")
    @ColumnWidth(30)
    private String component;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @ExcelProperty("菜单类型")
    @ColumnWidth(10)
    private String menuType;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    @ExcelProperty("菜单状态")
    @ColumnWidth(10)
    private String visible;

    /**
     * 权限标识
     */
    @ExcelProperty("权限标识")
    @ColumnWidth(30)
    private String perms;

    /**
     * 菜单图标
     */
    @ExcelProperty("菜单图标")
    @ColumnWidth(20)
    private String icon;
} 