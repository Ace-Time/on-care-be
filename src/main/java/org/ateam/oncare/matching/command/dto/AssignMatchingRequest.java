package org.ateam.oncare.matching.command.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AssignMatchingRequest {
    private Long beneficiaryId;
    private Long careWorkerId;

    private LocalDate effectiveDate;
}