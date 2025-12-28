package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.auth.security.JwtTokenProvider;
import org.ateam.oncare.careworker.command.dto.CreateCareLogRequest;
import org.ateam.oncare.careworker.command.dto.UpdateCareLogRequest;
import org.ateam.oncare.careworker.command.service.CareLogCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/care-logs")
@RequiredArgsConstructor
public class CareLogCommandController {

    private final CareLogCommandService careLogCommandService;
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

    // 1. 요양일지 작성
    @PostMapping
    public ApiResponse<Void> createCareLog(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateCareLogRequest request) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        careLogCommandService.createCareLog(employeeId, request);
        return ApiResponse.success(null);
    }

    // 2. 요양일지 수정
    @PatchMapping("/{logId}")
    public ApiResponse<Void> updateCareLog(
            @PathVariable Long logId,
            @RequestBody UpdateCareLogRequest request) {
        careLogCommandService.updateCareLog(logId, request);
        return ApiResponse.success(null);
    }

    // 3. 요양일지 삭제 (논리삭제)
    @DeleteMapping("/{logId}")
    public ApiResponse<Void> deleteCareLog(@PathVariable Long logId) {
        careLogCommandService.deleteCareLog(logId);
        return ApiResponse.success(null);
    }
}
