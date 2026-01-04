package org.ateam.oncare.counsel.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageData {

    private Integer stage;                      // 단계 번호 (1, 2, 3, 4)
    private Long potentialId;           // 잠재고객 ID
    private String processStatus;               // 처리 상태 ("P": 진행중, "F": 완료)
    private LocalDateTime processTime;          // 처리 시간
    private LocalDateTime createdAt;
    private Map<String, Object> stageData;      // 단계별 실제 데이터 (JSON)

}
