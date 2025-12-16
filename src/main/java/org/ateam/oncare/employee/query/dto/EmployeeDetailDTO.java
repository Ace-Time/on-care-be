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
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String emergencyNumber;
    private LocalDate hireDate;

    // 조인해서 가져올 데이터들
    private String deptName;    // 추가: 부서명
    private String jobName;     // 직책명
    private String statusField; // 추가: 상태명

    // 경력 리스트
    private List<CareerDTO> careers;

    // 자격증 리스트
    private List<CertificateViewDTO> certificates;

    // 제공 가능한 서비스 유형
    private List<ServiceTypeDTO> serviceTypes;

    // 보수 교육 이력
    private List<EducationDTO> educations;

    @Data
    public static class CareerDTO {
        private String companyName;
        private String workPeriod;
        private String task;
    }


}