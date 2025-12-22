package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.auth.security.JwtTokenProvider;
import org.ateam.oncare.careworker.command.dto.CompleteVisitRequest;
import org.ateam.oncare.careworker.command.dto.CreateVisitScheduleRequest;
import org.ateam.oncare.careworker.command.dto.StartVisitRequest;
import org.ateam.oncare.careworker.command.dto.UpdateVisitScheduleRequest;
import org.ateam.oncare.careworker.command.service.VisitScheduleCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visit-schedules")
@RequiredArgsConstructor
public class VisitScheduleCommandController {

    private final VisitScheduleCommandService visitScheduleCommandService;
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

    // 1. 방문 일정 서비스 시작 (SCHEDULED → IN_PROGRESS)
    @PostMapping("/{vsId}/start")
    public ApiResponse<Void> startVisit(
            @PathVariable Long vsId,
            @RequestBody StartVisitRequest request) {
        visitScheduleCommandService.startVisit(vsId, request);
        return ApiResponse.success(null);
    }

    // 2. 방문 일정 서비스 종료 (IN_PROGRESS → DONE)
    @PostMapping("/{vsId}/complete")
    public ApiResponse<Void> completeVisit(
            @PathVariable Long vsId,
            @RequestBody CompleteVisitRequest request) {
        visitScheduleCommandService.completeVisit(vsId, request);
        return ApiResponse.success(null);
    }

    // 3. 방문 요양 일정 작성
    @PostMapping
    public ApiResponse<Void> createVisitSchedule(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateVisitScheduleRequest request) {
        Long careWorkerId = getEmployeeIdFromToken(authHeader);
        visitScheduleCommandService.createVisitSchedule(careWorkerId, request);
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
