package org.ateam.oncare.employee.query.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeListDTO {
    private Long id;
    private String name;
    private String phone;

    private String deptName; // 부서명
    private String jobName; // 직책명
    private String statusField; // 상태명
    private String certificateNames; // 자격증 목록 (쉼표 구분)
    private String serviceTypeNames; // 서비스 유형 목록 (쉼표 구분)
    private Integer beneficiaryCount; // 담당 수급자 수
}