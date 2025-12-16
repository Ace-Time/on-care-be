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
}