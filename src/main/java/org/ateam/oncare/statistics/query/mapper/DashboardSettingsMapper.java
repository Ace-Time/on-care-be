package org.ateam.oncare.statistics.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.statistics.query.dto.DashboardSettingsDTO;

import java.time.LocalDateTime;

@Mapper
public interface DashboardSettingsMapper {
    // 직원 ID로 설정 조회
    DashboardSettingsDTO selectSettingsByEmployeeId(@Param("employeeId") int employeeId);

    // 설정 저장 (없으면 INSERT, 있으면 UPDATE)
    void upsertDashboardSettings(
            @Param("employeeId") int employeeId,
            @Param("configJson") String configJson,
            @Param("updatedAt") LocalDateTime updatedAt
    );
}
