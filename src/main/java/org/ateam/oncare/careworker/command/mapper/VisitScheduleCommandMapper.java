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
        int insertVisitSchedule(@Param("careWorkerId") Long careWorkerId,
                        @Param("request") CreateVisitScheduleRequest request);

        // 방문 요양 일정 수정
        int updateVisitSchedule(@Param("vsId") Long vsId, @Param("request") UpdateVisitScheduleRequest request);

        // 방문 요양 일정 삭제
        int deleteVisitSchedule(@Param("vsId") Long vsId);

        // 방문 일정의 요양보호사 ID 조회
        Long selectCareWorkerIdByVsId(@Param("vsId") Long vsId);

        // 방문 일정의 요양보호사(Employee) ID 조회
        Long selectEmployeeIdByVsId(@Param("vsId") Long vsId);

        // 형제 일정 조회 (동일 수급자, 요양보호사, 날짜)
        java.util.List<org.ateam.oncare.schedule.command.entity.VisitSchedule> selectVisitScheduleSiblings(
                        @Param("beneficiaryId") Long beneficiaryId,
                        @Param("careWorkerId") Long careWorkerId,
                        @Param("date") java.time.LocalDate date);

        // ID로 일정 단건 조회
        org.ateam.oncare.schedule.command.entity.VisitSchedule selectVisitScheduleById(@Param("vsId") Long vsId);
}
