package org.ateam.oncare.beneficiary.query.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.beneficiary.query.dto.response.CareLevelOptionResponse;
import org.ateam.oncare.beneficiary.query.dto.response.MetaOptionResponse;

import java.util.List;

/**
 * ✅ 마스터(태그/위험요소/등급) 조회 전용 Mapper
 */
@Mapper
public interface BeneficiaryMetaMapper {

    // 태그 마스터 정보
    List<MetaOptionResponse> selectTagOptions();

    // 위험요소 마스터 정보
    List<MetaOptionResponse> selectRiskFactorOptions();

    // 장기요양등급 마스터 정보
    List<CareLevelOptionResponse> selectCareLevelOptions();
}