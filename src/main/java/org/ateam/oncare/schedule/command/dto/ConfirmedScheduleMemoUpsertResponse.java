package org.ateam.oncare.schedule.command.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmedScheduleMemoUpsertResponse {
    private Long vsId;
    private String note;
}