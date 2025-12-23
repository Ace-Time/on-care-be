package org.ateam.oncare.beneficiary.command.dto.request;

/* 장기요양등급만료알림 연장예정 체크 변경 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateExtendsStatusRequest {
    // 'Y' or 'N'
    private String extendsStatus;
}