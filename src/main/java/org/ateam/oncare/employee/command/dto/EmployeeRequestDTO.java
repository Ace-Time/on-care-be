package org.ateam.oncare.employee.command.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeRequestDTO {
    // 기본 정보
    private String name;
    private LocalDate birth;
    private String gender;
    private String address; // 엔티티 필드명 유지
    private String email;
    private String phone;
    private String emergencyNumber;
    private LocalDate hireDate;
    private LocalDate endDate;

    // 외래 키(FK) 역할을 하는 ID 값들
    private Long deptCode;
    private Long jobCode;   // 예: 요양보호사 ID
    private Long managerId;
    private Long statusId;  // 예: 재직중 ID

    // 경력 리스트 (별도 테이블용)
    private List<CareerDTO> careers;
}
