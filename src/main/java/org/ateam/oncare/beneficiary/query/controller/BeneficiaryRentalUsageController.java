package org.ateam.oncare.beneficiary.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.RentalUsageResponse;
import org.ateam.oncare.beneficiary.query.service.BeneficiaryRentalUsageService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class BeneficiaryRentalUsageController {

    private final BeneficiaryRentalUsageService service;

    // 렌탈 탭 조회 (현재/과거)
    @GetMapping("/{beneficiaryId}/rentals")
    public RentalUsageResponse getRentals(@PathVariable Long beneficiaryId) {
        return service.getRentalUsage(beneficiaryId);
    }
}