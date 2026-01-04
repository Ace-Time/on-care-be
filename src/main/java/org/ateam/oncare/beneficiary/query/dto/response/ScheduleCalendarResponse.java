package org.ateam.oncare.beneficiary.query.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ScheduleCalendarResponse {

    private Integer year;
    private Integer month;
    private Long beneficiaryId;

    private List<Day> days = new ArrayList<>();

    @Getter
    @Setter
    public static class Day {
        private String date; // yyyy-MM-dd
        private List<Item> items = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class Item {
        private Long visitScheduleId;

        private String title;     // "09:00 방문(신체활동)"
        private String startDt;   // yyyy-MM-dd HH:mm:ss
        private String endDt;     // yyyy-MM-dd HH:mm:ss

        private Long serviceTypeId;
        private String serviceTypeName;

        private String visitStatus;    // DONE/SCHEDULED/IN_PROGRESS ...
        private String careWorkerName; // employee.name
        private String note;           // visit_schedule.note
    }
}