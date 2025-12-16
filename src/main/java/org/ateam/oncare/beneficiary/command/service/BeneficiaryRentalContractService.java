package org.ateam.oncare.beneficiary.command.service;


import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.command.mapper.BeneficiaryRentalContractMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BeneficiaryRentalContractService {

    private final BeneficiaryRentalContractMapper mapper;

    @Transactional
    public int completeRentalContract(Long beneficiaryId, Long rentalContractId) {
        return mapper.completeRentalContract(beneficiaryId, rentalContractId);
    }
}