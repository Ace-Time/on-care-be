package org.ateam.oncare.beneficiary.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.beneficiary.query.dto.response.BeneficiaryDetailResponse;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface BeneficiaryDetailMapper {

    BeneficiaryDetailResponse selectBeneficiaryDetail(@Param("beneficiaryId") Long beneficiaryId);
    List<String> selectBeneficiaryTags(@Param("beneficiaryId") Long beneficiaryId);

    List<BeneficiaryDetailResponse.RiskFactorItem> selectBeneficiaryRiskFactors(@Param("beneficiaryId") Long beneficiaryId);
}