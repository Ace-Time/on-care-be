package org.ateam.oncare.beneficiary.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.RentalContractDetailResponse;
import org.ateam.oncare.beneficiary.query.mapper.BeneficiaryRentalUsageMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeneficiaryRentalModalService {

    private final BeneficiaryRentalUsageMapper mapper;

    public RentalContractDetailResponse getRentalContractDetail(Long beneficiaryId, Long rentalContractId, String productAssetId) {
        return mapper.selectRentalContractDetail(beneficiaryId, rentalContractId, productAssetId);
    }
}