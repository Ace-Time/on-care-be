package org.ateam.oncare.statistics.query.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.statistics.query.dto.DashboardSettingsDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import org.ateam.oncare.statistics.query.mapper.DashboardSettingsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardSettingsMapper dashboardMapper;
    private final ObjectMapper objectMapper;

    @Override
    public DashboardSettingsDTO getSettings(int employeeId) {
        // 1. Mapper를 통해 DB 데이터 조회 (ResultMap핑된 객체)
        DashboardSettingsDTO dto = dashboardMapper.selectSettingsByEmployeeId(employeeId);

        List<Integer> widgetIds = new ArrayList<>();

        // 2. 조회된 데이터가 있고, JSON 문자열이 있다면 파싱
        if (dto != null && dto.getJsonConfig() != null) {
            try {
                // String(JSON) -> List<Integer> 변환
                widgetIds = objectMapper.readValue(dto.getJsonConfig(), new TypeReference<List<Integer>>() {});
            } catch (Exception e) {
                e.printStackTrace(); // 실제 운영에선 Logger 사용
            }
        } else {
            // 데이터가 없으면 빈 객체 생성
            dto = new DashboardSettingsDTO();
            dto.setEmployeeId(employeeId);
        }

        // 3. 변환된 리스트를 DTO에 담아서 리턴
        dto.setWidgetIds(widgetIds);
        return dto;
    }

    @Override
    @Transactional
    public void saveSettings(int employeeId, List<Integer> widgetIds) {
        try {
            // 1. List<Integer> -> String(JSON) 변환
            String jsonConfig = objectMapper.writeValueAsString(widgetIds);

            // 2. DB 저장 호출
            dashboardMapper.upsertDashboardSettings(employeeId, jsonConfig);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("설정 저장 중 오류 발생");
        }
    }
}