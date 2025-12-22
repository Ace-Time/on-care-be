package org.ateam.oncare.careworker.query.controller;

import org.ateam.oncare.auth.security.JwtTokenProvider;
import org.ateam.oncare.careworker.query.dto.*;
import org.ateam.oncare.careworker.query.service.DashboardQueryService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardQueryController {

    private final DashboardQueryService dashboardQueryService;
    private final JwtTokenProvider jwtTokenProvider;

    // JWT 토큰에서 사용자 ID 추출
    private Long getEmployeeIdFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = jwtTokenProvider.getClaimsFromAT(token);
            return claims.get("id", Long.class);
        }
        return 1L; // fallback
    }

    // 1. 요양보호사 정보 요약
    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryDto> getSummary(@RequestHeader("Authorization") String authHeader) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        DashboardSummaryDto data = dashboardQueryService.getSummary(employeeId);
        return ApiResponse.success(data);
    }

    // 2. 긴급 알림
    @GetMapping("/notifications/urgent")
    public ApiResponse<List<UrgentNotificationDto>> getUrgentNotifications(@RequestHeader("Authorization") String authHeader) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        List<UrgentNotificationDto> data = dashboardQueryService.getUrgentNotifications(employeeId);
        return ApiResponse.success(data);
    }

    // 3. 오늘의 일정
    @GetMapping("/schedules/today")
    public ApiResponse<List<HomeScheduleDto>> getTodaySchedules(@RequestHeader("Authorization") String authHeader) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        List<HomeScheduleDto> data = dashboardQueryService.getTodaySchedules(employeeId);
        return ApiResponse.success(data);
    }

    // 4. 할 일 목록
    @GetMapping("/todos")
    public ApiResponse<List<HomeTodoDto>> getTodos(@RequestHeader("Authorization") String authHeader) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        List<HomeTodoDto> data = dashboardQueryService.getTodos(employeeId);
        return ApiResponse.success(data);
    }

    // 5. 수급자 상세 정보 (토큰 X)
    @GetMapping("/beneficiary/{beneficiaryId}")
    public ApiResponse<BeneficiaryDetailDto> getBeneficiaryDetail(@PathVariable Long beneficiaryId) {
        BeneficiaryDetailDto data = dashboardQueryService.getBeneficiaryDetail(beneficiaryId);
        return ApiResponse.success(data);
    }

    // 6. 할 일 상세 정보 (토큰 X)
    @GetMapping("/todo/{todoId}")
    public ApiResponse<TodoDetailDto> getTodoDetail(@PathVariable Long todoId) {
        TodoDetailDto data = dashboardQueryService.getTodoDetail(todoId);
        return ApiResponse.success(data);
    }

    // 7. 특정 일정의 요양일지 조회 (토큰 X)
    @GetMapping("/schedule/{vsId}/carelog")
    public ApiResponse<CareLogDetailDto> getCareLogBySchedule(@PathVariable Long vsId) {
        CareLogDetailDto data = dashboardQueryService.getCareLogBySchedule(vsId);
        return ApiResponse.success(data);
    }
}