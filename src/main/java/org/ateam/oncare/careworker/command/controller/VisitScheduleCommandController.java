package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.careworker.command.dto.CompleteVisitRequest;
import org.ateam.oncare.careworker.command.dto.CreateVisitScheduleRequest;
import org.ateam.oncare.careworker.command.dto.StartVisitRequest;
import org.ateam.oncare.careworker.command.dto.UpdateVisitScheduleRequest;
import org.ateam.oncare.careworker.command.service.VisitScheduleCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visit-schedules")
@RequiredArgsConstructor
public class VisitScheduleCommandController {

    private final VisitScheduleCommandService visitScheduleCommandService;

    // 1. 방문 일정 서비스 시작 (SCHEDULED → IN_PROGRESS) - 현재 시간 자동 기록
    @PostMapping("/{vsId}/start")
    public ApiResponse<Void> startVisit(@PathVariable Long vsId) {
        visitScheduleCommandService.startVisit(vsId);
        return ApiResponse.success(null);
    }

    // 2. 방문 일정 서비스 종료 (IN_PROGRESS → DONE) - 현재 시간 자동 기록
    @PostMapping("/{vsId}/complete")
    public ApiResponse<Void> completeVisit(@PathVariable Long vsId) {
        visitScheduleCommandService.completeVisit(vsId);
        return ApiResponse.success(null);
    }

    // 3. 방문 요양 일정 작성
    @PostMapping
    public ApiResponse<Void> createVisitSchedule(
            @AuthenticationPrincipal EmployeeImpl employee,
            @RequestBody CreateVisitScheduleRequest request) {
        visitScheduleCommandService.createVisitSchedule(employee.getId(), request);
        return ApiResponse.success(null);
    }

    // 4. 방문 요양 일정 수정
    @PatchMapping("/{vsId}")
    public ApiResponse<Void> updateVisitSchedule(
            @PathVariable Long vsId,
            @RequestBody UpdateVisitScheduleRequest request) {
        visitScheduleCommandService.updateVisitSchedule(vsId, request);
        return ApiResponse.success(null);
    }

    // 5. 방문 요양 일정 삭제
    @DeleteMapping("/{vsId}")
    public ApiResponse<Void> deleteVisitSchedule(@PathVariable Long vsId) {
        visitScheduleCommandService.deleteVisitSchedule(vsId);
        return ApiResponse.success(null);
    }
}
