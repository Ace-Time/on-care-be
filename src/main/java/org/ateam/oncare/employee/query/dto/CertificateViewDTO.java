package org.ateam.oncare.employee.query.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CertificateViewDTO {
    private Long id; // Certificate ID

    private String employeeName;   // ★ [추가] 직원 이름

    private String certificateName; // 자격증 명
    private String organization; // 발급 기관
    private String licenseNo; // 면허 번호
    private LocalDate issueDate; // 발급일(신청일/발급일로 사용)
    private LocalDate expireDate; // 만료일
    private Integer status; // 상태 (0:대기, 2:승인)
}