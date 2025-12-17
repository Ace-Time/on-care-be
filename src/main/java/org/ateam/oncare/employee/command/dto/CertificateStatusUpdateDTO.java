package org.ateam.oncare.employee.command.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CertificateStatusUpdateDTO {

    // 프론트에서 보내는 상태값 (예: "APPROVED", "REJECTED", "PENDING")
    private String status;

    // (선택) 반려 사유 등 (예: "이미지가 흐릿함")
    // 현재 DB에 저장할 컬럼은 없지만, 로그를 남기거나 나중에 확장을 위해 받아둠
    private String reason;
}