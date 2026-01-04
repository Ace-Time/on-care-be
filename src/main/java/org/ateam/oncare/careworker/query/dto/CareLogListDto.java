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
    private Integer vsId;
    private LocalDate serviceDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String serviceType;
    private Long beneficiaryId;
    private String beneficiaryName;
    private String beneficiaryAddress;
    private String careLevel;
    private String specialNote;
    private Boolean isDraft;  // 임시저장 여부
}
