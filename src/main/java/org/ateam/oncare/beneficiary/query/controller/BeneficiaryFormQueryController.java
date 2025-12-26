package org.ateam.oncare.beneficiary.query.controller;

/* 수급자별 서류 목록 조회 */

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.FormListItem;
import org.ateam.oncare.beneficiary.query.service.FormQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class BeneficiaryFormQueryController {

    private final FormQueryService service;

    /**
     * 수급자별 서류 목록 조회
     * GET /api/beneficiaries/{beneficiaryId}/forms
     */
    @GetMapping("/{beneficiaryId}/forms")
    public List<FormListItem> getForms(@PathVariable Long beneficiaryId) {
        return service.getFormsForBeneficiary(beneficiaryId);
    }
}
