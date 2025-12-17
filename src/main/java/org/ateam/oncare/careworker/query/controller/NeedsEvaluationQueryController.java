package org.ateam.oncare.careworker.query.controller;

import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.careworker.query.dto.BasicEvaluationDetailDto;
import org.ateam.oncare.careworker.query.dto.BasicEvaluationListDto;
import org.ateam.oncare.careworker.query.service.BasicEvaluationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/needs-evaluations")
@RequiredArgsConstructor
public class NeedsEvaluationQueryController {

    private final BasicEvaluationQueryService basicEvaluationQueryService;
    private static final String EVAL_TYPE = "NEEDS";

    // 테스트용: 로그인한 유저 대신 ID를 1로 고정합니다.
    private final Long TEST_CAREGIVER_ID = 1L;

    // 1. 욕구사정 평가 목록 조회 (요양보호사별)
    @GetMapping
    public ApiResponse<List<BasicEvaluationListDto>> getNeedsEvaluationList(
            @RequestParam(required = false) Integer year) {
        List<BasicEvaluationListDto> data = basicEvaluationQueryService.getBasicEvaluationListByType(TEST_CAREGIVER_ID, EVAL_TYPE, year);
        return ApiResponse.success(data);
    }

    // 2. 욕구사정 평가 목록 조회 (수급자별)
    @GetMapping("/beneficiary/{beneficiaryId}")
    public ApiResponse<List<BasicEvaluationListDto>> getNeedsEvaluationListByBeneficiary(
            @PathVariable Long beneficiaryId,
            @RequestParam(required = false) Integer year) {
        List<BasicEvaluationListDto> data = basicEvaluationQueryService.getBasicEvaluationListByBeneficiaryAndType(beneficiaryId, EVAL_TYPE, year);
        return ApiResponse.success(data);
    }

    // 3. 욕구사정 평가 상세 조회
    @GetMapping("/{evalId}")
    public ApiResponse<BasicEvaluationDetailDto> getNeedsEvaluationDetail(@PathVariable Long evalId) {
        BasicEvaluationDetailDto data = basicEvaluationQueryService.getBasicEvaluationDetail(evalId);
        // 평가 유형이 일치하지 않으면 null 반환됨
        if (data != null && !EVAL_TYPE.equals(data.getEvalType())) {
            return ApiResponse.success(null);
        }
        return ApiResponse.success(data);
    }
}
