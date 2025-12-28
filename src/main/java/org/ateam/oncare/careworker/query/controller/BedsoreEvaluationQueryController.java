package org.ateam.oncare.careworker.query.controller;

import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.careworker.query.dto.BasicEvaluationDetailDto;
import org.ateam.oncare.careworker.query.dto.BasicEvaluationListDto;
import org.ateam.oncare.careworker.query.service.BasicEvaluationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bedsore-evaluations")
@RequiredArgsConstructor
public class BedsoreEvaluationQueryController {

    private final BasicEvaluationQueryService basicEvaluationQueryService;
    private static final String EVAL_TYPE = "BEDSORE";

    // 1. 욕창위험도 평가 목록 조회 (요양보호사별)
    @GetMapping
    public ApiResponse<List<BasicEvaluationListDto>> getBedsoreEvaluationList(
            @RequestHeader("Care-Worker-Id") Long careWorkerId,
            @RequestParam(required = false) Integer year) {
        List<BasicEvaluationListDto> data = basicEvaluationQueryService.getBasicEvaluationListByType(careWorkerId, EVAL_TYPE, year);
        return ApiResponse.success(data);
    }

    // 2. 욕창위험도 평가 목록 조회 (수급자별)
    @GetMapping("/beneficiary/{beneficiaryId}")
    public ApiResponse<List<BasicEvaluationListDto>> getBedsoreEvaluationListByBeneficiary(
            @PathVariable Long beneficiaryId,
            @RequestParam(required = false) Integer year) {
        List<BasicEvaluationListDto> data = basicEvaluationQueryService.getBasicEvaluationListByBeneficiaryAndType(beneficiaryId, EVAL_TYPE, year);
        return ApiResponse.success(data);
    }

    // 3. 욕창위험도 평가 상세 조회
    @GetMapping("/{evalId}")
    public ApiResponse<BasicEvaluationDetailDto> getBedsoreEvaluationDetail(@PathVariable Long evalId) {
        BasicEvaluationDetailDto data = basicEvaluationQueryService.getBasicEvaluationDetail(evalId);
        // 평가 유형이 일치하지 않으면 null 반환됨
        if (data != null && !EVAL_TYPE.equals(data.getEvalType())) {
            return ApiResponse.success(null);
        }
        return ApiResponse.success(data);
    }
}
