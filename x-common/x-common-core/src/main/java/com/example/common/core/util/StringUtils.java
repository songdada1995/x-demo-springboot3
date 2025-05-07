package com.example.common.core.util;

import java.util.UUID;

public class StringUtils {
    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
} 