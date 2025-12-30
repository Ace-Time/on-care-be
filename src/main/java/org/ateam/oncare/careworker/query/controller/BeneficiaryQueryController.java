package org.ateam.oncare.careworker.query.controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.auth.security.JwtTokenProvider;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.careworker.query.dto.MyBeneficiaryDto;
import org.ateam.oncare.careworker.query.service.BeneficiaryQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 요양보호사의 배정 수급자 조회 API
 */
@RestController
@RequestMapping("/api/careworker/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryQueryController {

    private final BeneficiaryQueryService beneficiaryQueryService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * JWT 토큰에서 사용자 ID 추출
     */
    private Long getEmployeeIdFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = jwtTokenProvider.getClaimsFromAT(token);
            return claims.get("id", Long.class);
        }
        return 1L; // fallback (개발용, 프로덕션에서는 예외 처리 필요)
    }

    /**
     * 로그인한 요양보호사에게 배정된 수급자 목록 조회
     * GET /api/careworker/beneficiaries
     */
    @GetMapping
    public ApiResponse<List<MyBeneficiaryDto>> getAssignedBeneficiaries(
            @RequestHeader("Authorization") String authHeader
    ) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        List<MyBeneficiaryDto> beneficiaries = beneficiaryQueryService.getAssignedBeneficiaries(employeeId);
        return ApiResponse.success(beneficiaries);
    }

    /**
     * 로그인한 요양보호사에게 배정된 특정 수급자 상세 조회
     * GET /api/careworker/beneficiaries/{beneficiaryId}
     */
    @GetMapping("/{beneficiaryId}")
    public ApiResponse<MyBeneficiaryDto> getAssignedBeneficiaryDetail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long beneficiaryId
    ) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        MyBeneficiaryDto beneficiary = beneficiaryQueryService.getAssignedBeneficiaryDetail(employeeId, beneficiaryId);

        if (beneficiary == null) {
            return ApiResponse.error(404, "배정되지 않은 수급자이거나 존재하지 않는 수급자입니다.");
        }

        return ApiResponse.success(beneficiary);
    }
}
