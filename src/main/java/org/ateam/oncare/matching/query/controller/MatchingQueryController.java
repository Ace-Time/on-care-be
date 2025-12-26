package org.ateam.oncare.matching.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.matching.query.dto.BeneficiarySummaryDto;
import org.ateam.oncare.matching.query.dto.BeneficiaryDetailDto;
import org.ateam.oncare.matching.query.dto.CareWorkerCardDto;
import org.ateam.oncare.matching.query.dto.CareWorkerDetailDto;
import org.ateam.oncare.matching.query.service.MatchingQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matching")
public class MatchingQueryController {

    private final MatchingQueryService matchingQueryService;

    /**
     * 최종 후보 요양보호사 카드 조회 (한방)
     * - 시간 겹침 X
     * - 수급자 서비스 유형 제공(전부 만족)
     * - 위험요소 대응 자격증 보유
     */
    @GetMapping("/careworkers/list")
    public ResponseEntity<List<CareWorkerCardDto>> getFinalCandidateCareWorkers(
            @RequestParam("beneficiaryId") Long beneficiaryId
    ) {
        List<Long> ids = matchingQueryService.selectFinalCandidateCareWorkerIds(beneficiaryId);
        return ResponseEntity.ok(matchingQueryService.getCareWorkerCardsByIds(ids));
    }

    @GetMapping("/beneficiaries/list")
    public ResponseEntity<List<BeneficiarySummaryDto>> getBeneficiariesList() {
        return ResponseEntity.ok(matchingQueryService.getBeneficiariesSummary());
    }

    @GetMapping("/beneficiaries/{beneficiaryId}")
    public ResponseEntity<BeneficiaryDetailDto> getBeneficiaryDetail(
            @PathVariable Long beneficiaryId
    ) {
        var detail = matchingQueryService.getBeneficiaryDetail(beneficiaryId);

        if (detail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/careworkers/{careWorkerId}")
    public ResponseEntity<CareWorkerDetailDto> getCareWorkerDetail(
            @PathVariable Long careWorkerId
    ) {
        var detail = matchingQueryService.getCareWorkerDetail(careWorkerId);

        if (detail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detail);
    }
}