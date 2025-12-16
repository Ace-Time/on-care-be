package org.ateam.oncare.counsel.query.controller;

import org.ateam.oncare.counsel.query.dto.CounselListResponse;
import org.ateam.oncare.counsel.query.dto.CustomerListResponse;
import org.ateam.oncare.counsel.query.service.CounselQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/counsel")
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

}
