package org.ateam.oncare.beneficiary.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.ServiceUsageResponse;
import org.ateam.oncare.beneficiary.query.service.BeneficiaryServiceUsageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class BeneficiaryServiceUsageController {

    private final BeneficiaryServiceUsageService service;

    // ✅ 서비스 탭 (월별 이력)
    @GetMapping("/{beneficiaryId}/services")
    public ServiceUsageResponse getServiceUsageHistory(
            @PathVariable Long beneficiaryId
    ) {
        return service.getServiceUsageHistory(beneficiaryId);
    }
}