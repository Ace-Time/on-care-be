package org.ateam.oncare.careworker.query.controller;

import org.ateam.oncare.auth.security.JwtTokenProvider;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.careworker.query.dto.BasicEvaluationDetailDto;
import org.ateam.oncare.careworker.query.dto.BasicEvaluationListDto;
import org.ateam.oncare.careworker.query.service.BasicEvaluationQueryService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cognitive-evaluations")
@RequiredArgsConstructor
public class CognitiveEvaluationQueryController {

    private final BasicEvaluationQueryService basicEvaluationQueryService;
    private final JwtTokenProvider jwtTokenProvider;
    private static final String EVAL_TYPE = "COGNITIVE";

    // JWT 토큰에서 사용자 ID 추출
    private Long getEmployeeIdFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = jwtTokenProvider.getClaimsFromAT(token);
            return claims.get("id", Long.class);
        }
        return 1L; // fallback
    }

    // 1. 인지기능 평가 목록 조회 (요양보호사별)
    @GetMapping
    public ApiResponse<List<BasicEvaluationListDto>> getCognitiveEvaluationList(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) Integer year) {
        Long employeeId = getEmployeeIdFromToken(authHeader);
        List<BasicEvaluationListDto> data = basicEvaluationQueryService.getBasicEvaluationListByType(employeeId, EVAL_TYPE, year);
        return ApiResponse.success(data);
    }

    // 2. 인지기능 평가 목록 조회 (수급자별)
    @GetMapping("/beneficiary/{beneficiaryId}")
    public ApiResponse<List<BasicEvaluationListDto>> getCognitiveEvaluationListByBeneficiary(
            @PathVariable Long beneficiaryId,
            @RequestParam(required = false) Integer year) {
        List<BasicEvaluationListDto> data = basicEvaluationQueryService.getBasicEvaluationListByBeneficiaryAndType(beneficiaryId, EVAL_TYPE, year);
        return ApiResponse.success(data);
    }

    // 3. 인지기능 평가 상세 조회
    @GetMapping("/{evalId}")
    public ApiResponse<BasicEvaluationDetailDto> getCognitiveEvaluationDetail(@PathVariable Long evalId) {
        BasicEvaluationDetailDto data = basicEvaluationQueryService.getBasicEvaluationDetail(evalId);
        // 평가 유형이 일치하지 않으면 null 반환됨
        if (data != null && !EVAL_TYPE.equals(data.getEvalType())) {
            return ApiResponse.success(null);
        }
        return ApiResponse.success(data);
    }
}
