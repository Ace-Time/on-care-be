package org.ateam.oncare.beneficiary.command.dto.request;

/* 장기요양등급만료알림 안내 완료 처리(사용자 입력) */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNoticeRequest {
    private String noticeDate; // 프론트에서 직접입력하면 받는 날짜
    private String memo;
    private Integer empId;
}