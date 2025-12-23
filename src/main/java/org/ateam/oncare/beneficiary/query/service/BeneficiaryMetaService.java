package org.ateam.oncare.beneficiary.query.service;


import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.CareLevelOptionResponse;
import org.ateam.oncare.beneficiary.query.dto.response.MetaOptionResponse;
import org.ateam.oncare.beneficiary.query.mapper.BeneficiaryMetaMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ✅ 마스터(태그/위험요소/등급) 조회 서비스
 * - 캐싱을 붙이고 싶으면 여기에 @Cacheable만 달면 됨
 */
@Service
@RequiredArgsConstructor
public class BeneficiaryMetaService {

    private final BeneficiaryMetaMapper mapper;

    public List<MetaOptionResponse> getTagOptions() {
        return mapper.selectTagOptions();
    }

    public List<MetaOptionResponse> getRiskFactorOptions() {
        return mapper.selectRiskFactorOptions();
    }

    public List<CareLevelOptionResponse> getCareLevelOptions() {
        return mapper.selectCareLevelOptions();
    }
}