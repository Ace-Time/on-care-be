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

    int updateRiskLevelByFactors(@Param("beneficiaryId") Long beneficiaryId);

    // ✅ 변경: 기존 endDate만 → endDate + number 같이
    int updateCareLevelInfo(@Param("beneficiaryId") Long beneficiaryId,
                            @Param("req") BeneficiaryUpdateRequest req);

    // ✅ 추가: 장기요양등급(조인테이블) 업데이트
    int updateCareLevelGrade(@Param("beneficiaryId") Long beneficiaryId,
                             @Param("careLevelId") Integer careLevelId);
}
