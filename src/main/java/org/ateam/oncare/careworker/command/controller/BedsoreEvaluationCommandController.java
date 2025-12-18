package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.careworker.command.dto.CreateBasicEvaluationRequest;
import org.ateam.oncare.careworker.command.dto.UpdateBasicEvaluationRequest;
import org.ateam.oncare.careworker.command.service.BasicEvaluationCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bedsore-evaluations")
@RequiredArgsConstructor
public class BedsoreEvaluationCommandController {

    private final BasicEvaluationCommandService basicEvaluationCommandService;
    private static final String EVAL_TYPE = "BEDSORE";

    // 테스트용
    private final Long TEST_CAREGIVER_ID = 1L;

    // 1. 욕창위험도 평가 작성
    @PostMapping
    public ApiResponse<Void> createBedsoreEvaluation(@RequestBody CreateBasicEvaluationRequest request) {
        request.setEvalType(EVAL_TYPE);
        basicEvaluationCommandService.createBasicEvaluation(TEST_CAREGIVER_ID, request);
        return ApiResponse.success(null);
    }

    // 2. 욕창위험도 평가 수정
    @PatchMapping("/{evalId}")
    public ApiResponse<Void> updateBedsoreEvaluation(
            @PathVariable Long evalId,
            @RequestBody UpdateBasicEvaluationRequest request) {
        basicEvaluationCommandService.updateBasicEvaluation(evalId, request);
        return ApiResponse.success(null);
    }

    // 3. 욕창위험도 평가 삭제
    @DeleteMapping("/{evalId}")
    public ApiResponse<Void> deleteBedsoreEvaluation(@PathVariable Long evalId) {
        basicEvaluationCommandService.deleteBasicEvaluation(evalId);
        return ApiResponse.success(null);
    }
}
