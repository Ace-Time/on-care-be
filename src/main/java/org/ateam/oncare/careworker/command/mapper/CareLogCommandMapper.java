package org.ateam.oncare.careworker.command.mapper;

import org.ateam.oncare.careworker.command.dto.CareLogInfo;
import org.ateam.oncare.careworker.command.dto.CreateCareLogRequest;
import org.ateam.oncare.careworker.command.dto.UpdateCareLogRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.time.LocalDate;

@Mapper
public interface CareLogCommandMapper {
        // 요양일지 작성
        int insertCareLog(
                        @Param("employeeId") Long employeeId,
                        @Param("request") CreateCareLogRequest request);

        // 요양일지 수정
        int updateCareLog(
                        @Param("logId") Long logId,
                        @Param("request") UpdateCareLogRequest request);

        // 요양일지 삭제 (논리삭제)
        int deleteCareLog(@Param("logId") Long logId);

        // 요양일지 정보 조회 (beneficiaryId, serviceDate)
        CareLogInfo selectCareLogInfo(@Param("logId") Long logId);

        // 방문 일정 삭제 시 관련 요양일지 삭제 (논리삭제)
        int deleteCareLogsByVsId(@Param("vsId") Long vsId);

        // 방문 일정 조회
        Long selectVsIdByBeneficiaryAndDate(@Param("beneficiaryId") Long beneficiaryId,
                        @Param("employeeId") Long employeeId,
                        @Param("serviceDate") LocalDate serviceDate);

        // 특정 근무 일정(vs_id)에 대한 요양일지 존재 여부 확인
        int countCareLogsByVsId(@Param("vsId") Long vsId);
}
