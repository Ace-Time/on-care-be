package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.auth.security.JwtTokenProvider;
import org.ateam.oncare.careworker.command.dto.CreatePersonalScheduleRequest;
import org.ateam.oncare.careworker.command.dto.UpdatePersonalScheduleRequest;
import org.ateam.oncare.careworker.command.service.PersonalScheduleCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal-schedules")
@RequiredArgsConstructor
public class PersonalScheduleCommandController {

    private final PersonalScheduleCommandService personalScheduleCommandService;
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

    // 1. 개인 일정 작성
    @PostMapping
    public ApiResponse<Void> createPersonalSchedule(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreatePersonalScheduleRequest request) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        personalScheduleCommandService.createPersonalSchedule(employeeId, request);
        return ApiResponse.success(null);
    }

    // 2. 개인 일정 수정
    @PatchMapping("/{psId}")
    public ApiResponse<Void> updatePersonalSchedule(
            @PathVariable Long psId,
            @RequestBody UpdatePersonalScheduleRequest request) {
        personalScheduleCommandService.updatePersonalSchedule(psId, request);
        return ApiResponse.success(null);
    }

    // 3. 개인 일정 삭제
    @DeleteMapping("/{psId}")
    public ApiResponse<Void> deletePersonalSchedule(@PathVariable Long psId) {
        personalScheduleCommandService.deletePersonalSchedule(psId);
        return ApiResponse.success(null);
    }
}
