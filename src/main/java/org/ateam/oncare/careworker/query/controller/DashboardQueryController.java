package org.ateam.oncare.careworker.query.controller;

import org.ateam.oncare.careworker.query.dto.*;
import org.ateam.oncare.careworker.query.service.DashboardQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardQueryController {

    private final DashboardQueryService dashboardQueryService;

    // ⚠️ 테스트용: 로그인한 유저 대신 ID를 1로 고정합니다.
    private final Long TEST_CAREGIVER_ID = 1L;

    // 1. 요양보호사 정보 요약 (토큰 X)
    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryDto> getSummary() {
        DashboardSummaryDto data = dashboardQueryService.getSummary(TEST_CAREGIVER_ID);
        return ApiResponse.success(data);
    }

    // 2. 긴급 알림 (토큰 X)
    @GetMapping("/notifications/urgent")
    public ApiResponse<List<UrgentNotificationDto>> getUrgentNotifications() {
        List<UrgentNotificationDto> data = dashboardQueryService.getUrgentNotifications(TEST_CAREGIVER_ID);
        return ApiResponse.success(data);
    }

    // 3. 오늘의 일정 (토큰 X)
    @GetMapping("/schedules/today")
    public ApiResponse<List<HomeScheduleDto>> getTodaySchedules() {
        List<HomeScheduleDto> data = dashboardQueryService.getTodaySchedules(TEST_CAREGIVER_ID);
        return ApiResponse.success(data);
    }

    // 4. 할 일 목록 (토큰 X)
    @GetMapping("/todos")
    public ApiResponse<List<HomeTodoDto>> getTodos() {
        List<HomeTodoDto> data = dashboardQueryService.getTodos(TEST_CAREGIVER_ID);
        return ApiResponse.success(data);
    }
}