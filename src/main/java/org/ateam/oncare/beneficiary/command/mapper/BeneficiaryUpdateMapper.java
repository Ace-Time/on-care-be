package org.ateam.oncare.beneficiary.command.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.command.dto.request.BeneficiaryUpdateRequest;

import java.util.List;

@Mapper
public interface BeneficiaryUpdateMapper {

    int updateBeneficiary(@Param("beneficiaryId") Long beneficiaryId,
                          @Param("req") BeneficiaryUpdateRequest req);

    int upsertGuardian(@Param("beneficiaryId") Long beneficiaryId,
                       @Param("req") BeneficiaryUpdateRequest req);

    int deleteBeneficiaryTags(@Param("beneficiaryId") Long beneficiaryId);

    int insertBeneficiaryTags(@Param("beneficiaryId") Long beneficiaryId,
                              @Param("tagIds") List<Long> tagIds);

    int deleteRiskFactors(@Param("beneficiaryId") Long beneficiaryId);

    int insertRiskFactors(@Param("beneficiaryId") Long beneficiaryId,
                          @Param("riskIds") List<Integer> riskIds);

    // ✅ 추가: 위험요소 기반 risk_id 갱신
    int updateRiskLevelByFactors(@Param("beneficiaryId") Long beneficiaryId);

    int updateCareLevelEndDate(@Param("beneficiaryId") Long beneficiaryId,
                               @Param("endDate") String endDate);
}
