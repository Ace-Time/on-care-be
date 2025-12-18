package org.ateam.oncare.employee.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CertificateInputDTO {
    private Long certificateId;   // 자격증 마스터 ID (예: 요양보호사 1급의 ID)
    private String licenseNo;     // 자격면허 번호
    private LocalDate issueDate;  // 발급일
    private LocalDate expireDate; // 만료일 (없으면 null)

    private List<EducationInputDTO> educations;
}
