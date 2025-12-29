package org.ateam.oncare.matching.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.matching.query.dto.*;
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
     * ✅ (케이스1) 수급자 기준 추천: 정렬 적용된 카드 리스트 반환
     */
    @GetMapping("/careworkers/list")
    public ResponseEntity<List<CareWorkerCardDto>> getFinalCandidateCareWorkers(
            @RequestParam("beneficiaryId") Long beneficiaryId
    ) {
        return ResponseEntity.ok(matchingQueryService.getCandidateCareWorkers(beneficiaryId));
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
        if (detail == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/careworkers/{careWorkerId}")
    public ResponseEntity<CareWorkerDetailDto> getCareWorkerDetail(
            @PathVariable Long careWorkerId
    ) {
        var detail = matchingQueryService.getCareWorkerDetail(careWorkerId);
        if (detail == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(detail);
    }

    /**
     * ✅ (케이스2) 방문일정 기준 추천: 정렬 적용된 카드 리스트 반환
     */
    @GetMapping("/careworkers/visit-available")
    public ResponseEntity<List<CareWorkerCardDto>> getVisitTimeAvailableCareWorkers(
            @RequestParam("vsId") Long vsId,
            @RequestParam("startDt") String startDt,
            @RequestParam("endDt") String endDt
    ) {
        return ResponseEntity.ok(
                matchingQueryService.getVisitTimeAvailableCareWorkers(vsId, startDt, endDt)
        );
    }

    /**
     * ✅ (케이스3) 방문일정 생성 추천: 정렬 적용된 카드 리스트 반환
     */
    @GetMapping("/careworkers/visit-create-available")
    public ResponseEntity<List<CareWorkerCardDto>> getCreateVisitAvailableCareWorkers(
            @RequestParam("beneficiaryId") Long beneficiaryId,
            @RequestParam("serviceTypeId") Long serviceTypeId,
            @RequestParam("startDt") String startDt,
            @RequestParam("endDt") String endDt
    ) {
        return ResponseEntity.ok(
                matchingQueryService.getCreateVisitAvailableCareWorkers(
                        beneficiaryId, serviceTypeId, startDt, endDt
                )
        );
    }
}