package org.ateam.oncare.beneficiary.query.dto.response;

// 요양일지 일지 리스트


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CareLogListResponse {

    private Long logId;           // 요양일지번호
    private Long beneficiaryId;   // 수급자ID

    private String recordedAt;    // 기록일자 (created_at)
    private String serviceDate;   // 서비스일자
    private String startTime;     // 시작시간
    private String endTime;       // 종료시간

    private String serviceType;   // 서비스구분
    private Integer careWorkerId; // 요양보호사ID
    private String careWorkerName;// 요양보호사 이름
}