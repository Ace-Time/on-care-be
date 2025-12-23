package org.ateam.oncare.beneficiary.query.dto.response;

// 서비스탭 3번 화면

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceVisitRecordResponse {

    private String month; // YYYY-MM
    private Integer serviceTypeId;
    private String serviceTypeName; // optional
    private List<VisitRecordItem> records;

    @Getter
    @Setter
    public static class VisitRecordItem {
        private Integer recordId;
        private Integer visitScheduleId;

        private String workDate;   // YYYY-MM-DD
        private String startTime;  // HH:mm
        private String endTime;    // HH:mm
        private Double hours;      // ✅ 방문시간(시간)

        private Integer serviceTypeId;
        private String serviceTypeName;

        private Integer amount;    // calculated_amount

        private String note;           // ✅ visit_schedule.note
        private String careWorkerName; // ✅ employee.name
    }
}