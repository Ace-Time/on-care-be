package org.ateam.oncare.employee.query.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleResDTO {
    private String id;
    private String title;
    private String start;
    private String end;
    private String backgroundColor;
    private String borderColor;
    private String textColor;
    private Boolean allDay;
    private ExtendedProps extendedProps;

    @Data
    public static class ExtendedProps {
        private String type;
        private String status;

        public ExtendedProps(String type, String status) {
            this.type = type;
            this.status = status;
        }
    }
}
