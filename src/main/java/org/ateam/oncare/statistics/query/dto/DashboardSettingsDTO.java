package org.ateam.oncare.statistics.query.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DashboardSettingsDTO {
    private Integer id;             // PK
    private Integer employeeId;     // 직원 ID

    // 프론트엔드와 주고받을 실제 데이터 (List 형태)
    private List<Integer> widgetIds;

    // 프론트엔드로 응답 보낼 때는 이 필드를 숨김 (불필요하므로)
    @JsonIgnore
    private String jsonConfig;
}