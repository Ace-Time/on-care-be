package org.ateam.oncare.global.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 클라이언트의 ip를 확인하는 메소드
 * proxy를 거칠 경우 header 명칭이 달라질 경우를 대비해 범용적으로 사용되는 header들을 확인
 */
public class ClientIpUtils {
    private ClientIpUtils() { throw new IllegalStateException("Utility class"); }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        // 1. 표준 헤더 (쉼표 분리 포함)
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.contains(",")) return ip.split(",")[0].trim();
            return ip;
        }
        // 2. 비표준 헤더 Fallback
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        // 3. 기본 IP
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();

        return ip;
    }
}
