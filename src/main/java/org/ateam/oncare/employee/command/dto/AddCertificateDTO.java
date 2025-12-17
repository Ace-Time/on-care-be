package org.ateam.oncare.employee.command.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCertificateDTO {
    private Long certificateId;   // 자격증 마스터 ID (예: 1=요양보호사)
    private String licenseNo;
    private LocalDate issueDate;
    private LocalDate expireDate;
}