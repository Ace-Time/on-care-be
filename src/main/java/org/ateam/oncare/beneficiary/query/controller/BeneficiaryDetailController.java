package org.ateam.oncare.beneficiary.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.BeneficiaryDetailResponse;
import org.ateam.oncare.beneficiary.query.service.BeneficiaryDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class BeneficiaryDetailController {

    private final BeneficiaryDetailService beneficiaryDetailService;

    // 우측 상단 상세 카드
    @GetMapping("/{beneficiaryId}")
    public BeneficiaryDetailResponse getBeneficiaryDetail(@PathVariable Long beneficiaryId) {
        return beneficiaryDetailService.getDetail(beneficiaryId);
    }
}