package org.ateam.oncare.employee.query.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EducationDTO {
    private Long id;
    private String eduName;      // 교육명
    private String institution;  // 교육기관
    private LocalDate eduDate;   // 교육일
    private LocalDate nextEduDate; // 다음 교육 예정일
    private Boolean isOverdue;   // 미이수 여부
    private Integer status;      // 승인 상태
    private String relatedLicenseName; // (선택) 어떤 자격증 관련 교육인지 표시
}
