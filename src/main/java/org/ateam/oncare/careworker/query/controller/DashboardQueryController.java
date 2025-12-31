package org.ateam.oncare.careworker.query.controller;

import org.ateam.oncare.careworker.query.dto.*;
import org.ateam.oncare.careworker.query.service.DashboardQueryService;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardQueryController {

    private final DashboardQueryService dashboardQueryService;

    // 1. 요양보호사 정보 요약
    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryDto> getSummary(@AuthenticationPrincipal EmployeeImpl employee) {
        DashboardSummaryDto data = dashboardQueryService.getSummary(employee.getId());
        return ApiResponse.success(data);
    }

    // 2. 긴급 알림 (제거됨)
    // @GetMapping("/notifications/urgent")
    // public ApiResponse<List<UrgentNotificationDto>>
    // getUrgentNotifications(@AuthenticationPrincipal EmployeeImpl employee) {
    // List<UrgentNotificationDto> data =
    // dashboardQueryService.getUrgentNotifications(employee.getId());
    // return ApiResponse.success(data);
    // }

    // 3. 오늘의 일정
    @GetMapping("/schedules/today")
    public ApiResponse<List<HomeScheduleDto>> getTodaySchedules(@AuthenticationPrincipal EmployeeImpl employee) {
        List<HomeScheduleDto> data = dashboardQueryService.getTodaySchedules(employee.getId());
        return ApiResponse.success(data);
    }

    // 4. 할 일 목록
    @GetMapping("/todos")
    public ApiResponse<List<HomeTodoDto>> getTodos(@AuthenticationPrincipal EmployeeImpl employee) {
        List<HomeTodoDto> data = dashboardQueryService.getTodos(employee.getId());
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

    // 8. 내 수급자 목록 조회
    @GetMapping("/my-beneficiaries")
    public ApiResponse<List<MyBeneficiaryDto>> getMyBeneficiaries(@AuthenticationPrincipal EmployeeImpl employee) {
        List<MyBeneficiaryDto> data = dashboardQueryService.getMyBeneficiaries(employee.getId());
        return ApiResponse.success(data);
    }
}