package org.ateam.oncare.careworker.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCounselingLogRequest {
    private LocalDate counselingDate;
    private String counselingType;
    private String satisfaction;
    private String visitPurpose;
    private String attendees;
    private String discussionContent;
    private String agreementContent;
    private LocalDate nextVisitDate;
    private String counselorSignUrl;
    private String guardianSignUrl;
    private Long beneficiaryId;
    private Boolean isDraft = false;  // 임시저장 여부
}
