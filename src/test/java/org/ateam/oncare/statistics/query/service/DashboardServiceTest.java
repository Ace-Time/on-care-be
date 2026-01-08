package org.ateam.oncare.statistics.query.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ateam.oncare.statistics.query.dto.DashboardSettingsDTO;
import org.ateam.oncare.statistics.query.mapper.DashboardSettingsMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private DashboardSettingsMapper dashboardMapper;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    @DisplayName("대시보드 설정 저장 테스트")
    void testSaveDashboardSettings() throws JsonProcessingException {
        // Given
        int empId = 1;
        List<Integer> widgetIds = List.of(10, 20, 30);
        String jsonOutput = "[10,20,30]";

        when(objectMapper.writeValueAsString(widgetIds)).thenReturn(jsonOutput);

        // When
        dashboardService.saveSettings(empId, widgetIds);

        // Then
        verify(dashboardMapper, times(1)).upsertDashboardSettings(eq(empId), eq(jsonOutput), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("대시보드 설정 조회 테스트")
    void testGetDashboardSettings() throws JsonProcessingException {
        // Given
        int empId = 1;
        List<Integer> expectedWidgets = List.of(1, 2);

        DashboardSettingsDTO dtoFromDb = new DashboardSettingsDTO();
        dtoFromDb.setEmployeeId(empId);
        dtoFromDb.setJsonConfig("[1,2]");

        when(dashboardMapper.selectSettingsByEmployeeId(empId)).thenReturn(dtoFromDb);
        when(objectMapper.readValue(eq("[1,2]"), any(com.fasterxml.jackson.core.type.TypeReference.class)))
                .thenReturn(expectedWidgets);

        // When
        DashboardSettingsDTO resultDto = dashboardService.getSettings(empId);

        // Then
        assertEquals(expectedWidgets, resultDto.getWidgetIds());
    }

    @Test
    @DisplayName("대시보드 설정 조회 - 데이터가 없을 경우 기본값 반환 테스트")
    void testGetSettings_NoData() {
        // Given
        int empId = 2;
        when(dashboardMapper.selectSettingsByEmployeeId(empId)).thenReturn(null);

        // When
        DashboardSettingsDTO resultDto = dashboardService.getSettings(empId);

        // Then
        // 데이터가 없으면 빈 DTO가 리턴됨
        org.junit.jupiter.api.Assertions.assertNotNull(resultDto);
        assertEquals(empId, resultDto.getEmployeeId());
        org.junit.jupiter.api.Assertions.assertTrue(resultDto.getWidgetIds().isEmpty());
    }

    @Test
    @DisplayName("대시보드 설정 조회 - 잘못된 JSON 데이터 처리 테스트")
    void testGetSettings_InvalidJson() throws JsonProcessingException {
        // Given
        int empId = 3;
        DashboardSettingsDTO dtoFromDb = new DashboardSettingsDTO();
        dtoFromDb.setEmployeeId(empId);
        dtoFromDb.setJsonConfig("{invalid_json}");

        when(dashboardMapper.selectSettingsByEmployeeId(empId)).thenReturn(dtoFromDb);
        // JSON 파싱 에러 발생 시뮬레이션
        when(objectMapper.readValue(eq("{invalid_json}"), any(com.fasterxml.jackson.core.type.TypeReference.class)))
                .thenThrow(new com.fasterxml.jackson.core.JsonParseException(null, "Error"));

        // When
        DashboardSettingsDTO resultDto = dashboardService.getSettings(empId);

        // Then
        // 에러 발생 시 빈 리스트 반환 (Service 로직 상 catch해서 빈 리스트 유지)
        org.junit.jupiter.api.Assertions.assertTrue(resultDto.getWidgetIds().isEmpty());
    }

    @Test
    @DisplayName("대시보드 설정 저장 중 예외 발생 테스트")
    void testSaveSettings_Exception() throws JsonProcessingException {
        // Given
        int empId = 1;
        List<Integer> widgetIds = List.of(1, 2);

        // ObjectMapper에서 에러가 발생한다고 가정
        when(objectMapper.writeValueAsString(widgetIds)).thenThrow(new RuntimeException("JSON Error"));

        // When & Then
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> dashboardService.saveSettings(empId, widgetIds));
    }
}
