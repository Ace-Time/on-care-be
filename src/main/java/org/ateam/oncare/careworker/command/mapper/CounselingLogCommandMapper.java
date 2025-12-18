package org.ateam.oncare.careworker.command.mapper;

import org.ateam.oncare.careworker.command.dto.CreateCounselingLogRequest;
import org.ateam.oncare.careworker.command.dto.UpdateCounselingLogRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CounselingLogCommandMapper {
    // 방문상담 작성
    int insertCounselingLog(
            @Param("careWorkerId") Long careWorkerId,
            @Param("request") CreateCounselingLogRequest request);

    // 방문상담 수정
    int updateCounselingLog(
            @Param("counselingId") Long counselingId,
            @Param("request") UpdateCounselingLogRequest request);

    // 방문상담 삭제
    int deleteCounselingLog(@Param("counselingId") Long counselingId);
}
