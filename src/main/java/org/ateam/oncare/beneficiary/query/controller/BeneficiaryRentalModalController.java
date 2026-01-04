package org.ateam.oncare.beneficiary.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.RentalContractDetailResponse;
import org.ateam.oncare.beneficiary.query.service.BeneficiaryRentalModalService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class BeneficiaryRentalModalController {

    private final BeneficiaryRentalModalService service;

    // 모달 상세 조회
    @GetMapping("/{beneficiaryId}/rentals/{rentalContractId}/products/{productAssetId}")
    public RentalContractDetailResponse getRentalContractDetail(
            @PathVariable Long beneficiaryId,
            @PathVariable Long rentalContractId,
            @PathVariable String productAssetId
    ) {
        return service.getRentalContractDetail(beneficiaryId, rentalContractId, productAssetId);
    }
}