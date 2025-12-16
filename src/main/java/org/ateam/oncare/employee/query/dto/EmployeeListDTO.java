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
    private String jobName;    // jobCode -> m_job 테이블 조인 결과
    private String statusName; // statusId -> m_status 테이블 조인 결과
    private String statusField; // 상태(활동중/휴가 등)
}