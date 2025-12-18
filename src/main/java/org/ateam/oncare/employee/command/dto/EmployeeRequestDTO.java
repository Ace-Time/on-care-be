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
public class EmployeeRequestDTO {
    // --- 기본 정보 ---
    private String name;
    private String address; // 오타 수정됨 (adress -> address)
    private String phone;
    private String email;
    private LocalDate birth;
    private String gender;
    private String emergencyNumber;
    private LocalDate hireDate;
    private LocalDate endDate;

    // --- [중요] 연관 테이블의 ID(PK) 값만 받습니다 ---
    // 이름(예: "간호팀")을 받는 게 아니라 ID(예: 1)를 받습니다.
    private Integer deptCode; // m_department 테이블의 id
    private Long jobCode; // m_job 테이블의 id
    private Long statusId; // m_status 테이블의 status (PK)
    private Long managerId; // 매니저 직원 ID

    // 경력 리스트
    private List<CareerDTO> careers;

    // 보유 자격증 리스트
    private List<CertificateInputDTO> certificates;

    // 서비스 유형 ID 리스트 (예: [1, 2])
    private List<Long> serviceTypeIds;
}