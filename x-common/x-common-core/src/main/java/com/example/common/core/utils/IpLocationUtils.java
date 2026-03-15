package com.example.common.core.utils;

import org.springframework.util.StringUtils;

import java.net.InetAddress;

/**
 * IP 归属地工具类（简化实现）
 */
public final class IpLocationUtils {

    private IpLocationUtils() {
    }

    /**
     * 获取 IP 归属地（不依赖外部IP库）
     *
     * @param ip IP地址
     * @return 归属地描述
     */
    public static String resolveLocation(String ip) {
        if (!StringUtils.hasText(ip)) {
            return "未知";
        }

        String normalized = ip.trim();
        try {
            InetAddress address = InetAddress.getByName(normalized);
            if (address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isSiteLocalAddress()) {
                return "内网IP";
            }
            return "公网IP";
        } catch (Exception ex) {
            return "未知地区";
        }
    }
}
