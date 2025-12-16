package org.ateam.oncare.employee.query.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeSearchCondition {
    private String keyword;  // 검색어 (이름 또는 전화번호)
    private Long jobCode;    // 직군 필터
    private Long statusId;   // 상태 필터
}