package org.ateam.oncare.careworker.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CounselingLogListDto {
    private Long counselingId;
    private LocalDate counselingDate;
    private String counselingType;
    private String satisfaction;
    private Long beneficiaryId;
    private String beneficiaryName;
    private String careLevel;
    private Boolean isDraft;  // 임시저장 여부
}
