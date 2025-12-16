package org.ateam.oncare.careworker.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CareLogListDto {
    private Long logId;
    private LocalDate serviceDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String serviceType;
    private Long beneficiaryId;
    private String beneficiaryName;
    private String careLevel;
}
