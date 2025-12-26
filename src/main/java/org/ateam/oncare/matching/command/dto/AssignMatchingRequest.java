package org.ateam.oncare.matching.command.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AssignMatchingRequest {
    private Long beneficiaryId;
    private Long careWorkerId;
}