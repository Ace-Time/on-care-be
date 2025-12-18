package org.ateam.oncare.careworker.command.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface VisitScheduleCommandMapper {
    // 방문 일정 서비스 시작
    int updateVisitStatusToInProgress(
            @Param("vsId") Long vsId,
            @Param("actualStartTime") LocalDateTime actualStartTime);

    // 방문 일정 서비스 종료
    int updateVisitStatusToCompleted(
            @Param("vsId") Long vsId,
            @Param("actualEndTime") LocalDateTime actualEndTime);
}
