package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CounselHistoryDetailResponse {
    private Long counselHistoryId;
    private String consultDate;      // yyyy-MM-dd

    private Long beneficiaryId;
    private String beneficiaryName;

    private String categoryName;

    private Integer counselorId;
    private String counselorName;
    private String summary;

    private String detail;           // 상담 내용
    private String followUp;         // 후속 조치
}