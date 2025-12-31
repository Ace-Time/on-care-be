package org.ateam.oncare.statistics.query.service;

import org.ateam.oncare.statistics.query.dto.DashboardSettingsDTO;

import java.util.List;

public interface DashboardService {
    /**
     * 직원 ID로 대시보드 설정을 조회합니다.
     */
    DashboardSettingsDTO getSettings(int employeeId);

    /**
     * 직원 ID의 대시보드 설정을 저장(또는 수정)합니다.
     */
    void saveSettings(int employeeId, List<Integer> widgetIds);
}