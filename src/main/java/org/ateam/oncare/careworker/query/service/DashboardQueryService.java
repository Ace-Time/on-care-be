package org.ateam.oncare.careworker.query.service;

import org.ateam.oncare.careworker.query.dto.*;
import org.ateam.oncare.careworker.query.mapper.DashboardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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

    public BeneficiaryDetailDto getBeneficiaryDetail(Long beneficiaryId) {
        BeneficiaryDetailDto detail = dashboardMapper.selectBeneficiaryDetail(beneficiaryId);

        if (detail != null) {
            // 보호자 정보 설정
            detail.setGuardians(dashboardMapper.selectGuardians(beneficiaryId));

            // 위험요소 설정
            detail.setRiskFactors(dashboardMapper.selectRiskFactors(beneficiaryId));

            // 태그 설정
            detail.setTags(dashboardMapper.selectTags(beneficiaryId));

            // 특이사항 설정
            detail.setSignificants(dashboardMapper.selectSignificants(beneficiaryId));
        }

        return detail;
    }

    public TodoDetailDto getTodoDetail(Long todoId) {
        return dashboardMapper.selectTodoDetail(todoId);
    }

    public CareLogDetailDto getCareLogBySchedule(Long vsId) {
        return dashboardMapper.selectCareLogBySchedule(vsId);
    }

    public List<MyBeneficiaryDto> getMyBeneficiaries(Long employeeId) {
        try {
            return dashboardMapper.selectMyBeneficiaries(employeeId);
        } catch (Exception e) {
            log.error("내 수급자 목록 조회 중 오류 발생 - employeeId: {}", employeeId, e);
            throw e;
        }
    }
}