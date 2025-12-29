package org.ateam.oncare.employee.query.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EducationAlertDTO {
    private Long employeeId; // 클릭 시 해당 직원 상세로 이동용
    private String name; // 직원 이름 (예: 이영희)
    private LocalDate nextEduDate; // 다음 교육 예정일
    private Integer dDay; // 남은 일수 (음수면 초과, 양수면 남음)
    private String status; // 'OVERDUE'(초과) or 'WARNING'(임박)
}
