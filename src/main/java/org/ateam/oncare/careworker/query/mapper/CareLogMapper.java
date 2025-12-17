package org.ateam.oncare.careworker.query.mapper;

import org.ateam.oncare.careworker.query.dto.CareLogDetailDto;
import org.ateam.oncare.careworker.query.dto.CareLogListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CareLogMapper {
    // 요양일지 목록 조회 (요양보호사별)
    List<CareLogListDto> selectCareLogList(@Param("caregiverId") Long caregiverId);

    // 요양일지 목록 조회 (수급자별)
    List<CareLogListDto> selectCareLogListByBeneficiary(@Param("beneficiaryId") Long beneficiaryId);

    // 요양일지 상세 조회
    CareLogDetailDto selectCareLogDetail(@Param("logId") Long logId);
}
