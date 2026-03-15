package com.example.common.core.enums;

/**
 * 业务操作类型枚举
 */
public enum BusinessType {

    OTHER(0, "其它"),
    INSERT(1, "新增"),
    UPDATE(2, "修改"),
    DELETE(3, "删除"),
    SELECT(4, "查询"),
    EXPORT(5, "导出"),
    IMPORT(6, "导入"),
    GRANT(7, "授权"),
    FORCE(8, "强退"),
    CLEAN(9, "清空数据");

    private final int code;
    private final String desc;

    BusinessType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
