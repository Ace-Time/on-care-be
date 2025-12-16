package org.ateam.oncare.beneficiary.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.BeneficiaryDetailResponse;
import org.ateam.oncare.beneficiary.query.mapper.BeneficiaryDetailMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeneficiaryDetailService {

    private final BeneficiaryDetailMapper beneficiaryDetailMapper;

    public BeneficiaryDetailResponse getDetail(Long beneficiaryId) {
        return beneficiaryDetailMapper.selectBeneficiaryDetail(beneficiaryId);
    }
}