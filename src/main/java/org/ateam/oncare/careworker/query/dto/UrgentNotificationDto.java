package org.ateam.oncare.careworker.query.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter @Setter
public class UrgentNotificationDto {
    private Long id;
    private String title;
    private String message;
    private LocalDate dueDate;
    private boolean isUrgent;
}
