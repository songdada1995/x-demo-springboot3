package com.example.common.core.enums;

/**
 * 操作人类别枚举
 */
public enum OperatorType {

    OTHER(0, "其它"),
    MANAGE(1, "后台用户"),
    MOBILE(2, "手机端用户");

    private final int code;
    private final String desc;

    OperatorType(int code, String desc) {
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
