package org.ateam.oncare.beneficiary.command.dto.request;

/* 장기요양등급만료알림 안내 완료 처리(사용자 입력) */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNoticeRequest {
    // "2025-12-16 10:00:00" 같은 문자열로 받거나 프론트에서 ISO로 보내도 됨
    private String noticeDate;
    private String memo;
    private Integer empId;
}