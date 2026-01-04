package org.ateam.oncare.beneficiary.query.controller.ai;


import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.ai.MonthlySummaryAiResponse;
import org.ateam.oncare.beneficiary.query.service.ai.CareLogMonthlyAiSummaryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai/beneficiaries")
public class CareLogAiSummaryController {

    private final CareLogMonthlyAiSummaryService service;

    /**
     * ✅ 월별 AI 요약 생성
     * POST /ai/beneficiaries/{beneficiaryId}/care-logs/monthly-summary?month=2025-12
     */
    @PostMapping("/{beneficiaryId}/care-logs/monthly-summary")
    public MonthlySummaryAiResponse generateMonthlySummary(
            @PathVariable Long beneficiaryId,
            @RequestParam String month
    ) {
        return service.generateMonthlySummary(beneficiaryId, month);
    }
}