package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.careworker.command.dto.CreateBasicEvaluationRequest;
import org.ateam.oncare.careworker.command.dto.UpdateBasicEvaluationRequest;
import org.ateam.oncare.careworker.command.service.BasicEvaluationCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fall-evaluations")
@RequiredArgsConstructor
public class FallEvaluationCommandController {

    private final BasicEvaluationCommandService basicEvaluationCommandService;
    private static final String EVAL_TYPE = "FALL";

    // 테스트용
    private final Long TEST_CAREGIVER_ID = 1L;

    // 1. 낙상위험도 평가 작성
    @PostMapping
    public ApiResponse<Void> createFallEvaluation(@RequestBody CreateBasicEvaluationRequest request) {
        request.setEvalType(EVAL_TYPE);
        basicEvaluationCommandService.createBasicEvaluation(TEST_CAREGIVER_ID, request);
        return ApiResponse.success(null);
    }

    // 2. 낙상위험도 평가 수정
    @PatchMapping("/{evalId}")
    public ApiResponse<Void> updateFallEvaluation(
            @PathVariable Long evalId,
            @RequestBody UpdateBasicEvaluationRequest request) {
        basicEvaluationCommandService.updateBasicEvaluation(evalId, request);
        return ApiResponse.success(null);
    }

    // 3. 낙상위험도 평가 삭제
    @DeleteMapping("/{evalId}")
    public ApiResponse<Void> deleteFallEvaluation(@PathVariable Long evalId) {
        basicEvaluationCommandService.deleteBasicEvaluation(evalId);
        return ApiResponse.success(null);
    }
}
