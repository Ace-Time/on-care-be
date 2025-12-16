package org.ateam.oncare.careworker.query.mapper;

import org.ateam.oncare.careworker.query.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DashboardMapper {
    // 요약 정보 조회
    DashboardSummaryDto selectDashboardSummary(@Param("caregiverId") Long caregiverId);

    // 긴급 알림 목록
    List<UrgentNotificationDto> selectUrgentNotifications(@Param("caregiverId") Long caregiverId);

    // 오늘의 일정 목록
    List<HomeScheduleDto> selectTodaySchedules(@Param("caregiverId") Long caregiverId);

    // 할 일 목록
    List<HomeTodoDto> selectTodos(@Param("caregiverId") Long caregiverId);

    // 수급자 상세 정보 조회
    BeneficiaryDetailDto selectBeneficiaryDetail(@Param("beneficiaryId") Long beneficiaryId);

    // 수급자 보호자 목록
    List<BeneficiaryDetailDto.GuardianDto> selectGuardians(@Param("beneficiaryId") Long beneficiaryId);

    // 수급자 위험요소 목록
    List<String> selectRiskFactors(@Param("beneficiaryId") Long beneficiaryId);

    // 수급자 태그 목록
    List<String> selectTags(@Param("beneficiaryId") Long beneficiaryId);

    // 수급자 특이사항 목록
    List<String> selectSignificants(@Param("beneficiaryId") Long beneficiaryId);

    // 할 일 상세 정보 조회
    TodoDetailDto selectTodoDetail(@Param("todoId") Long todoId);

    // 특정 일정의 요양일지 조회
    CareLogDetailDto selectCareLogBySchedule(@Param("vsId") Long vsId);
}