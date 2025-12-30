package org.ateam.oncare.careworker.query.mapper;

import org.ateam.oncare.careworker.query.dto.MyBeneficiaryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BeneficiaryQueryMapper {

    /**
     * 요양보호사에게 배정된 수급자 목록 조회
     * @param employeeId 요양보호사 직원 ID
     * @return 배정된 수급자 목록
     */
    List<MyBeneficiaryDto> selectAssignedBeneficiaries(@Param("employeeId") Long employeeId);

    /**
     * 요양보호사에게 배정된 특정 수급자 상세 조회
     * @param employeeId 요양보호사 직원 ID
     * @param beneficiaryId 수급자 ID
     * @return 수급자 상세 정보
     */
    MyBeneficiaryDto selectAssignedBeneficiaryDetail(
            @Param("employeeId") Long employeeId,
            @Param("beneficiaryId") Long beneficiaryId
    );
}
