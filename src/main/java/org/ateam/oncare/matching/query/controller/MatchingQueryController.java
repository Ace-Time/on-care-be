package org.ateam.oncare.matching.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.matching.query.dto.*;
import org.ateam.oncare.matching.query.service.MatchingQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching")
public class MatchingQueryController {

    private final MatchingQueryService matchingQueryService;

    /**
     * ✅ (케이스1) 수급자 기준 추천: 정렬 적용된 카드 리스트 반환
     */
    @GetMapping("/careworkers/list")
    public ResponseEntity<CareWorkerPageResponse> getFinalCandidateCareWorkers(
            @RequestParam Long beneficiaryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(
                matchingQueryService.getCandidateCareWorkersPage(
                        beneficiaryId, page, size, keyword
                )
        );
    }

    @GetMapping("/beneficiaries/list")
    public ResponseEntity<BeneficiaryPageResponse> getBeneficiariesList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String assigned
    ) {
        return ResponseEntity.ok(
                matchingQueryService.getBeneficiariesPage(page, size, keyword, assigned)
        );
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
    public ResponseEntity<CareWorkerPageResponse> getVisitTimeAvailableCareWorkers(
            @RequestParam("vsId") Long vsId,
            @RequestParam("startDt") String startDt,
            @RequestParam("endDt") String endDt,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(
                matchingQueryService.getVisitTimeAvailableCareWorkersPage(vsId, startDt, endDt, page)
        );
    }

    /**
     * ✅ (케이스3) 방문일정 생성 추천: 정렬 적용된 카드 리스트 반환
     */
    @GetMapping("/careworkers/visit-create-available")
    public ResponseEntity<CareWorkerPageResponse> getCreateVisitAvailableCareWorkers(
            @RequestParam("beneficiaryId") Long beneficiaryId,
            @RequestParam("serviceTypeId") Long serviceTypeId,
            @RequestParam("startDt") String startDt,
            @RequestParam("endDt") String endDt,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "8") int size
    ) {
        return ResponseEntity.ok(
                matchingQueryService.getCreateVisitAvailableCareWorkersPage(
                        beneficiaryId, serviceTypeId, startDt, endDt, page, size
                )
        );
    }
}