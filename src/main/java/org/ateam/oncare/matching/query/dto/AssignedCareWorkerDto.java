package org.ateam.oncare.matching.query.dto;

import lombok.Data;

@Data
public class AssignedCareWorkerDto {
    private Long careWorkerId;
    private String name;       // 박민수
    private String gender;     // 남자/여자
    private String startDate;  // "2024-11-18" (배정일)
}