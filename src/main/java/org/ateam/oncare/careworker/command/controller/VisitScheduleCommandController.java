package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.careworker.command.dto.CompleteVisitRequest;
import org.ateam.oncare.careworker.command.dto.StartVisitRequest;
import org.ateam.oncare.careworker.command.service.VisitScheduleCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visit-schedules")
@RequiredArgsConstructor
public class VisitScheduleCommandController {

    private final VisitScheduleCommandService visitScheduleCommandService;

    // 1. 방문 일정 서비스 시작 (SCHEDULED → IN_PROGRESS)
    @PostMapping("/{vsId}/start")
    public ApiResponse<Void> startVisit(
            @PathVariable Long vsId,
            @RequestBody StartVisitRequest request) {
        visitScheduleCommandService.startVisit(vsId, request);
        return ApiResponse.success(null);
    }

    // 2. 방문 일정 서비스 종료 (IN_PROGRESS → COMPLETED)
    @PostMapping("/{vsId}/complete")
    public ApiResponse<Void> completeVisit(
            @PathVariable Long vsId,
            @RequestBody CompleteVisitRequest request) {
        visitScheduleCommandService.completeVisit(vsId, request);
        return ApiResponse.success(null);
    }
}
