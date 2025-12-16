package org.ateam.oncare.counsel.query.controller;

import org.ateam.oncare.counsel.query.dto.CounselListResponse;
import org.ateam.oncare.counsel.query.dto.CustomerListResponse;
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
public class CounselQueryController {
    private final CounselQueryService counselQueryService;

    @Autowired
    public CounselQueryController(CounselQueryService counselQueryService) {
        this.counselQueryService = counselQueryService;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerListResponse>> requestCustomerList(
            @RequestParam(value = "keyword", required = false)String keyword) {
        return ResponseEntity.ok(counselQueryService.searchCustomers(keyword));
    }

    @GetMapping("/customers/{customerId}/counsels")
    public ResponseEntity<Slice<CounselListResponse>> requestCounselList(
            @PathVariable("customerId") BigInteger customerId,
            @RequestParam("customerType") String customerType,
            @PageableDefault(page = 0, size = 5, sort = "consult_date", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(counselQueryService.findCounselsByCustomerId(customerId, customerType, pageable));
    }

}
