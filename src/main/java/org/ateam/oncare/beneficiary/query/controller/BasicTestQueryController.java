package org.ateam.oncare.beneficiary.query.controller;

// 기초평가

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.*;
import org.ateam.oncare.beneficiary.query.service.BasicTestQueryService;
import org.ateam.oncare.beneficiary.query.dto.response.ApiOptionalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class BasicTestQueryController {

    private final BasicTestQueryService service;

    @GetMapping("/{beneficiaryId}/basic-evaluations/fall/latest")
    public ResponseEntity<ApiOptionalResponse<FallEvaluationLatestResponse>>
    getFallLatest(@PathVariable Long beneficiaryId) {
        return ResponseEntity.ok(service.getFallLatest(beneficiaryId));
    }

    @GetMapping("/{beneficiaryId}/basic-evaluations/bedsore/latest")
    public ResponseEntity<ApiOptionalResponse<BedsoreEvaluationLatestResponse>>
    getBedsoreLatest(@PathVariable Long beneficiaryId) {
        return ResponseEntity.ok(service.getBedsoreLatest(beneficiaryId));
    }

    @GetMapping("/{beneficiaryId}/basic-evaluations/cognitive/latest")
    public ResponseEntity<ApiOptionalResponse<CognitiveEvaluationLatestResponse>>
    getCognitiveLatest(@PathVariable Long beneficiaryId) {
        return ResponseEntity.ok(service.getCognitiveLatest(beneficiaryId));
    }

    @GetMapping("/{beneficiaryId}/basic-evaluations/needs/latest")
    public ResponseEntity<ApiOptionalResponse<NeedsEvaluationLatestResponse>>
    getNeedsLatest(@PathVariable Long beneficiaryId) {
        return ResponseEntity.ok(service.getNeedsLatest(beneficiaryId));
    }
}
