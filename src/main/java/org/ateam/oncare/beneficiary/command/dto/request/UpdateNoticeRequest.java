package org.ateam.oncare.beneficiary.command.dto.request;

/* 장기요양등급만료알림 안내 이력 변경 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNoticeRequest {
    private String noticeDate;
    private String memo;
    private Integer empId;
}