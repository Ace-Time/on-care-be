package org.ateam.oncare.careworker.query.controller;

import org.ateam.oncare.auth.security.JwtTokenProvider;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.careworker.query.dto.CareLogDetailDto;
import org.ateam.oncare.careworker.query.dto.CareLogListDto;
import org.ateam.oncare.careworker.query.service.CareLogQueryService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/care-logs")
@RequiredArgsConstructor
public class CareLogQueryController {

    private final CareLogQueryService careLogQueryService;
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

    // 1. 요양일지 목록 조회 (요양보호사별)
    @GetMapping
    public ApiResponse<List<CareLogListDto>> getCareLogList(
            @RequestHeader("Authorization") String authHeader) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        List<CareLogListDto> data = careLogQueryService.getCareLogList(employeeId);
        return ApiResponse.success(data);
    }

    // 2. 요양일지 목록 조회 (수급자별)
    @GetMapping("/beneficiary/{beneficiaryId}")
    public ApiResponse<List<CareLogListDto>> getCareLogListByBeneficiary(@PathVariable Long beneficiaryId) {
        List<CareLogListDto> data = careLogQueryService.getCareLogListByBeneficiary(beneficiaryId);
        return ApiResponse.success(data);
    }

    // 3. 요양일지 상세 조회
    @GetMapping("/{logId}")
    public ApiResponse<CareLogDetailDto> getCareLogDetail(@PathVariable Long logId) {
        CareLogDetailDto data = careLogQueryService.getCareLogDetail(logId);
        return ApiResponse.success(data);
    }
}
