package org.ateam.oncare.employee.query.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeDetailDTO {
    // 기본 정보
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String adress; // 오타 유지
    private String emergencyNumber;
    private LocalDate hireDate;
    private Integer careerYears; // 경력 연수 (계산 필요 시)

    // 조인된 정보
    private String jobName;
    private String deptName; // 부서명 (있다면)

    // 리스트 정보
    private List<CareerDTO> careers;

    @Data
    public static class CareerDTO {
        private String companyName;
        private String workPeriod;
        private String task;
    }
}