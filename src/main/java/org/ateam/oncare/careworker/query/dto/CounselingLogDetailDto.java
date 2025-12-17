package org.ateam.oncare.careworker.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CounselingLogDetailDto {
    // 기본 정보
    private Long counselingId;
    private LocalDate counselingDate;
    private String counselingType;
    private String satisfaction;
    private LocalDateTime createdAt;

    // 수급자 정보
    private Long beneficiaryId;
    private String beneficiaryName;
    private String beneficiaryAddress;
    private String careLevel;

    // 상담 내용
    private String visitPurpose;
    private String attendees;
    private String discussionContent;
    private String agreementContent;
    private LocalDate nextVisitDate;

    // 서명
    private String counselorSignUrl;
    private String guardianSignUrl;
}
