package org.ateam.oncare.beneficiary.query.controller.ai;

// AI요약 최신 월 조회(백엔드)

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.AiCareSummaryResponse;
import org.ateam.oncare.beneficiary.query.service.ai.AiCareQueryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class AiCareQueryController {

    private final AiCareQueryService service;

    /**
     * ✅ 월별 AI 요약 "최신 1건" 조회
     * GET /ai/beneficiaries/{beneficiaryId}/care-logs/monthly-summary?month=2025-12
     */
    @GetMapping("/{beneficiaryId}/care-logs/monthly-summary")
    public AiCareSummaryResponse getLatestMonthlySummary(
            @PathVariable Long beneficiaryId,
            @RequestParam String month
    ) {
        return service.getLatestMonthlySummary(beneficiaryId, month);
    }
}