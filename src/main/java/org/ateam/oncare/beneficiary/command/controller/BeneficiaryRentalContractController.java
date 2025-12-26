package org.ateam.oncare.beneficiary.command.controller;


import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.command.dto.response.RentalContractCompleteResponse;
import org.ateam.oncare.beneficiary.command.service.BeneficiaryRentalContractService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class BeneficiaryRentalContractController {

    private final BeneficiaryRentalContractService service;

    // ✅ 계약완료로 변경
    @PatchMapping("/{beneficiaryId}/rentals/{rentalContractId}/complete")
    public RentalContractCompleteResponse completeRentalContract(
            @PathVariable Long beneficiaryId,
            @PathVariable Long rentalContractId
    ) {
        return service.completeRentalContract(beneficiaryId, rentalContractId);
    }
}