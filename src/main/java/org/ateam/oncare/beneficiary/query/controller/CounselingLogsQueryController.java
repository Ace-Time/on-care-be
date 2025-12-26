package org.ateam.oncare.beneficiary.query.controller;


import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.CounselingDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.CounselingListResponse;
import org.ateam.oncare.beneficiary.query.service.CounselingLogsQueryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class CounselingLogsQueryController {

    private final CounselingLogsQueryService service;

    // 1) 상담 탭 목록 조회
    @GetMapping("/{beneficiaryId}/counselings")
    public CounselingListResponse getCounselingList(@PathVariable Long beneficiaryId) {
        return service.getCounselingList(beneficiaryId);
    }

    // 2) 상담 상세 모달 조회
    @GetMapping("/{beneficiaryId}/counselings/{counselingId}")
    public CounselingDetailResponse getCounselingDetail(
            @PathVariable Long beneficiaryId,
            @PathVariable Long counselingId
    ) {
        return service.getCounselingDetail(beneficiaryId, counselingId);
    }
}