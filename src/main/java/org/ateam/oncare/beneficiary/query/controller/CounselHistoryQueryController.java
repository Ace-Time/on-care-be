package org.ateam.oncare.beneficiary.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.CounselHistoryDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.CounselHistoryListResponse;
import org.ateam.oncare.beneficiary.query.service.CounselHistoryQueryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries/{beneficiaryId}/counsel-histories")
public class CounselHistoryQueryController {

    private final CounselHistoryQueryService counselHistoryQueryService;

    // 1) 문의이력 탭 조회
    @GetMapping
    public CounselHistoryListResponse getList(@PathVariable Long beneficiaryId) {
        return counselHistoryQueryService.getCounselHistoryList(beneficiaryId);
    }

    // 2) 문의이력 상세(모달)
    @GetMapping("/{counselHistoryId}")
    public CounselHistoryDetailResponse getDetail(
            @PathVariable Long beneficiaryId,
            @PathVariable Long counselHistoryId
    ) {
        return counselHistoryQueryService.getCounselHistoryDetail(beneficiaryId, counselHistoryId);
    }
}