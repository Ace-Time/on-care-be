package org.ateam.oncare.careworker.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateVisitScheduleRequest {
    private Long beneficiaryId;
    private Long serviceTypeId;  // 하위 호환성을 위해 유지 (단일 서비스 유형)
    private java.util.List<Long> serviceTypeIds;  // 여러 서비스 유형 (우선 사용)
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private String visitStatus;
    private String note;
}
