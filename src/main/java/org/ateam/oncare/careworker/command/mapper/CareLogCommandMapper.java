package org.ateam.oncare.careworker.command.mapper;

import org.ateam.oncare.careworker.command.dto.CreateCareLogRequest;
import org.ateam.oncare.careworker.command.dto.UpdateCareLogRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
