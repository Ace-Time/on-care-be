package org.ateam.oncare.counsel.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.counsel.query.dto.*;
import org.ateam.oncare.counsel.query.service.CounselQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/counsel")
@Validated
@RequiredArgsConstructor
public class CounselQueryController {
    private final CounselQueryService counselQueryService;

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerListResponse>> requestCustomerList(
            @RequestParam(value = "keyword", required = false)String keyword) {
        return ResponseEntity.ok(counselQueryService.searchCustomers(keyword));
    }

    @GetMapping("/customers/{customerId}/counsels")
    public ResponseEntity<Slice<CounselListResponse>> requestCounselList(
            @PathVariable("customerId") BigInteger customerId,
            @RequestParam("customerType") String customerType,
            @RequestParam(value = "counselCategoryName", required = false) String counselCategoryName,
            @PageableDefault(page = 0, size = 5, sort = "consult_date", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(counselQueryService.findCounselsByCustomerId(customerId, customerType, counselCategoryName, pageable));
    }

    @GetMapping("/{counselHistoryId}")
    public ResponseEntity<CounselDetailResponse> requestCounselDetail(@PathVariable("counselHistoryId")BigInteger counselHistoryId) {
        return ResponseEntity.ok(counselQueryService.findCounselDetailById(counselHistoryId));
    }

    @GetMapping("/beneficiaries/{beneficiaryId}/significants")
    public ResponseEntity<List<BeneficiarySignificantResponse>> getBeneficiarySignificants(
            @PathVariable("beneficiaryId") BigInteger beneficiaryId,
            @RequestParam(value = "category", required = false) String category
    ) {
        return ResponseEntity.ok(counselQueryService.findBeneficiarySignificants(beneficiaryId, category));
    }

    @GetMapping("/significants/master")
    public ResponseEntity<List<SignificantMasterResponse>> getAllSignificants() {
        return ResponseEntity.ok(counselQueryService.findAllSignificants());
    }


}
