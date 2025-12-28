package org.ateam.oncare.careworker.query.mapper;

import org.ateam.oncare.careworker.query.dto.CounselingLogDetailDto;
import org.ateam.oncare.careworker.query.dto.CounselingLogListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CounselingLogMapper {
    // 방문상담 목록 조회 (요양보호사별)
    List<CounselingLogListDto> selectCounselingLogList(@Param("employeeId") Long employeeId);

    // 방문상담 목록 조회 (수급자별)
    List<CounselingLogListDto> selectCounselingLogListByBeneficiary(@Param("beneficiaryId") Long beneficiaryId);

    // 방문상담 상세 조회
    CounselingLogDetailDto selectCounselingLogDetail(@Param("counselingId") Long counselingId);
}
