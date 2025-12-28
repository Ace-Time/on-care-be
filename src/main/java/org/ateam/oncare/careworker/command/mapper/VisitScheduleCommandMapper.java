package org.ateam.oncare.careworker.command.mapper;

import org.ateam.oncare.careworker.command.dto.CreateVisitScheduleRequest;
import org.ateam.oncare.careworker.command.dto.UpdateVisitScheduleRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VisitScheduleCommandMapper {
    // 방문 일정 서비스 시작
    int updateVisitStatusToInProgress(@Param("vsId") Long vsId);

    // 방문 일정 서비스 종료
    int updateVisitStatusToCompleted(@Param("vsId") Long vsId);

    // 방문 요양 일정 작성
    int insertVisitSchedule(@Param("careWorkerId") Long careWorkerId, @Param("request") CreateVisitScheduleRequest request);

    // 방문 요양 일정 수정
    int updateVisitSchedule(@Param("vsId") Long vsId, @Param("request") UpdateVisitScheduleRequest request);

    // 방문 요양 일정 삭제
    int deleteVisitSchedule(@Param("vsId") Long vsId);
}
