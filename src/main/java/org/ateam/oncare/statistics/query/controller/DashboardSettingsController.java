package org.ateam.oncare.statistics.query.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.statistics.query.dto.DashboardSettingsDTO;
import org.ateam.oncare.statistics.query.service.DashboardService;
import org.springframework.web.bind.annotation.*;
import org.ateam.oncare.auth.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardSettingsController {

    private final DashboardService dashboardService;
    private final JwtTokenProvider jwtTokenProvider;

    // JWT 토큰에서 사용자 ID 추출 (실패 시 null 반환)
    private Integer getEmployeeIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        try {
            if (jwtTokenProvider.validateAccessToken(token)) {
                Claims claims = jwtTokenProvider.getClaimsFromAT(token);
                Object idObj = claims.get("id");

                if (idObj != null) {
                    return Integer.parseInt(String.valueOf(idObj));
                }
            }
        } catch (Exception e) {
            log.error("DashboardSettingsController - Token parsing error", e);
        }
        return null;
    }

    // 1. 설정 불러오기 (GET /api/dashboard/settings)
    @GetMapping("/settings")
    public DashboardSettingsDTO getDashboardSettings(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        Integer employeeId = getEmployeeIdFromToken(authHeader);

        DashboardSettingsDTO defaultDto = new DashboardSettingsDTO();
        defaultDto.setWidgetIds(Collections.emptyList());

        if (employeeId == null) {
            // 로그인 정보가 없을 경우 기본값 반환
            return defaultDto;
        }

        try {
            DashboardSettingsDTO settings = dashboardService.getSettings(employeeId);
            if (settings == null) {
                return defaultDto;
            }
            return settings;
        } catch (Exception e) {
            log.error("Failed to load dashboard settings from DB for user: {}", employeeId, e);
            return defaultDto;
        }
    }

    // 2. 설정 저장하기 (POST /api/dashboard/settings)
    @PostMapping("/settings")
    public void saveDashboardSettings(HttpServletRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, List<Integer>> payload) {

        // 프론트엔드 디버깅용: 들어오는 모든 헤더 로그 출력
        log.info("=== POST /api/dashboard/settings Request Headers ===");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info("Header -> {}: {}", headerName, request.getHeader(headerName));
        }
        log.info("==================================================");

        Integer employeeId = getEmployeeIdFromToken(authHeader);

        if (employeeId == null) {
            log.error("Authentication failed. Authorization header found: {}", authHeader);
            throw new RuntimeException("로그인이 필요한 서비스입니다.");
        }

        List<Integer> widgetIds = payload.get("widgetIds");
        dashboardService.saveSettings(employeeId, widgetIds);
    }
}