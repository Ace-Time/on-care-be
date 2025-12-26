package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CounselingDetailResponse {

    private Long counselingId;

    private String counselingDate;     // yyyy-MM-dd
    private String counselingType;

    private String careWorkerName;     // 요양보호사 이름
    private String beneficiaryName;    // 수급자 이름
    private Long beneficiaryId;        // 수급자 번호

    private String attendees;          // 참석 가족

    private String visitPurpose;
    private String discussionContent;
    private String agreementContent;

    private String nextVisitDate;      // yyyy-MM-dd (nullable)
}