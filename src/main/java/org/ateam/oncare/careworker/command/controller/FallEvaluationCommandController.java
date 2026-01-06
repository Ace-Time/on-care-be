package org.ateam.oncare.careworker.command.controller;

import org.ateam.oncare.careworker.command.dto.CreateBasicEvaluationRequest;
import org.ateam.oncare.careworker.command.dto.UpdateBasicEvaluationRequest;
import org.ateam.oncare.careworker.command.service.BasicEvaluationCommandService;
import org.ateam.oncare.careworker.query.dto.ApiResponse;
import org.ateam.oncare.employee.command.dto.EmployeeImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fall-evaluations")
@RequiredArgsConstructor
public class FallEvaluationCommandController {

    private final BasicEvaluationCommandService basicEvaluationCommandService;
    private static final String EVAL_TYPE = "FALL";

    // 1. 낙상위험도 평가 작성
    @PostMapping
    public ApiResponse<Void> createFallEvaluation(
            @AuthenticationPrincipal EmployeeImpl employee,
            @RequestBody String requestBody) {
        System.out.println(">>> [DEBUG] Raw JSON: " + requestBody);

        // Debugging: Manual deserialization
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            CreateBasicEvaluationRequest dto = mapper.readValue(requestBody, CreateBasicEvaluationRequest.class);
            System.out.println(">>> [DEBUG] Manually Mapped DTO: " + dto);

            dto.setEvalType(EVAL_TYPE);
            basicEvaluationCommandService.createBasicEvaluation(employee.getId(), dto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("JSON Parsing Failed", e);
        }

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
