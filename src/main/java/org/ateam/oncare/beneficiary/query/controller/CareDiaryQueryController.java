package org.ateam.oncare.beneficiary.query.controller;


import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.CareLogDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.CareLogListResponse;
import org.ateam.oncare.beneficiary.query.service.CareDiaryQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class CareDiaryQueryController {

    private final CareDiaryQueryService service;

    /**
     * ✅ 요양일지 리스트 (월 기준)
     * GET /api/beneficiaries/{beneficiaryId}/care-logs?month=2025-12
     */
    @GetMapping("/{beneficiaryId}/care-logs")
    public List<CareLogListResponse> getCareLogList(
            @PathVariable Long beneficiaryId,
            @RequestParam(required = false) String month
    ) {
        return service.getCareLogList(beneficiaryId, month);
    }

    /**
     * ✅ 요양일지 상세
     * GET /api/beneficiaries/{beneficiaryId}/care-logs/{logId}
     */
    @GetMapping("/{beneficiaryId}/care-logs/{logId}")
    public CareLogDetailResponse getCareLogDetail(
            @PathVariable Long beneficiaryId,
            @PathVariable Long logId
    ) {
        return service.getCareLogDetail(beneficiaryId, logId);
    }
}