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

    // ▼ 아래 3개 필드가 꼭 있어야 합니다! ▼
    private String deptName;    // 부서명
    private String jobName;     // 직책명
    private String statusField; // 상태명
}