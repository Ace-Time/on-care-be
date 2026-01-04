package org.ateam.oncare.beneficiary.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.ServiceTypeSummaryResponse;
import org.ateam.oncare.beneficiary.query.dto.response.ServiceUsageResponse;
import org.ateam.oncare.beneficiary.query.dto.response.ServiceVisitRecordResponse;
import org.ateam.oncare.beneficiary.query.service.BeneficiaryServiceUsageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class BeneficiaryServiceUsageController {

    private final BeneficiaryServiceUsageService service;

    // ✅ 1번 화면: 월별 전체 누계
    @GetMapping("/{beneficiaryId}/services")
    public ServiceUsageResponse getServiceUsageHistory(
            @PathVariable Long beneficiaryId
    ) {
        return service.getServiceUsageHistory(beneficiaryId);
    }

    // ✅ 2번 화면: 선택한 월의 서비스유형별 누계
    // 예) /api/beneficiaries/1/services/2025-12/types
    @GetMapping("/{beneficiaryId}/services/{month}/types")
    public ServiceTypeSummaryResponse getServiceTypeSummary(
            @PathVariable Long beneficiaryId,
            @PathVariable String month
    ) {
        return service.getServiceTypeSummary(beneficiaryId, month);
    }

    // ✅ 3번 화면: 선택한 월 + 선택한 서비스유형 방문 리스트
    // 예) /api/beneficiaries/1/services/2025-12/types/1/records
    @GetMapping("/{beneficiaryId}/services/{month}/types/{serviceTypeId}/records")
    public ServiceVisitRecordResponse getVisitRecords(
            @PathVariable Long beneficiaryId,
            @PathVariable String month,
            @PathVariable Integer serviceTypeId
    ) {
        return service.getVisitRecords(beneficiaryId, month, serviceTypeId);
    }
}