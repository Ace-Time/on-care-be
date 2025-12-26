package org.ateam.oncare.beneficiary.query.mapper;

// 일정관리 탭
// Mapper 결과 Row DTO (쿼리 결과 받는 용도)

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarFixedVisitRow {
    private Long visitScheduleId;

    private String startDt; // yyyy-MM-dd HH:mm:ss
    private String endDt;   // yyyy-MM-dd HH:mm:ss

    private String visitStatus;
    private String note;

    private Long serviceTypeId;
    private String serviceTypeName;

    private Long careWorkerId;
    private String careWorkerName;
}