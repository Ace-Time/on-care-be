package org.ateam.oncare.beneficiary.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.command.dto.request.BeneficiaryUpdateRequest;
import org.ateam.oncare.beneficiary.command.dto.response.BeneficiaryUpdateResponse;
import org.ateam.oncare.beneficiary.command.service.BeneficiaryUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class BeneficiaryUpdateController {

    private final BeneficiaryUpdateService beneficiaryUpdateService;

    @PutMapping("/{beneficiaryId}")
    public ResponseEntity<BeneficiaryUpdateResponse> updateBeneficiary(
            @PathVariable Long beneficiaryId,
            @RequestBody BeneficiaryUpdateRequest request
    ) {
        BeneficiaryUpdateResponse response = beneficiaryUpdateService.update(beneficiaryId, request);
        return ResponseEntity.ok(response);
    }
}
