package org.ateam.oncare.careworker.query.service;

import org.ateam.oncare.careworker.query.dto.*;
import org.ateam.oncare.careworker.query.mapper.DashboardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용으로 성능 최적화
public class DashboardQueryService {

    private final DashboardMapper dashboardMapper;

    public DashboardSummaryDto getSummary(Long caregiverId) {
        return dashboardMapper.selectDashboardSummary(caregiverId);
    }

    public List<UrgentNotificationDto> getUrgentNotifications(Long caregiverId) {
        return dashboardMapper.selectUrgentNotifications(caregiverId);
    }

    public List<HomeScheduleDto> getTodaySchedules(Long caregiverId) {
        return dashboardMapper.selectTodaySchedules(caregiverId);
    }

    public List<HomeTodoDto> getTodos(Long caregiverId) {
        return dashboardMapper.selectTodos(caregiverId);
    }
}