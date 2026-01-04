package org.ateam.oncare.schedule.query.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmedScheduleMemoDto {
    private Integer vsId;
    private String note;
}