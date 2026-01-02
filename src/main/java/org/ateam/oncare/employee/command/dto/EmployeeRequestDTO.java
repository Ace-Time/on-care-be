package org.ateam.oncare.employee.command.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    private String currentPassword; // 현재 비밀번호 (본인 확인용)
    private String newPassword; // 변경할 새 비밀번호

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phone;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotNull(message = "생년월일은 필수 입력 항목입니다.")
    private LocalDate birth;

    private String gender;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "비상연락처 형식이 올바르지 않습니다.")
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

    // 보유 자격증 리스트 - 중첩된 객체도 검증하기 위해 @Valid 추가
    @Valid
    private List<CertificateInputDTO> certificates;

    // 서비스 유형 ID 리스트 (예: [1, 2])
    private List<Long> serviceTypeIds;

    // 만 20세 이상인지 검증하는 커스텀 로직
    @AssertTrue(message = "만 20세 이상만 직원으로 등록 가능합니다.")
    public boolean isAgeValid() {
        if (birth == null)
            return false;

        // 오늘 날짜
        LocalDate today = LocalDate.now();

        // 제한일: 오늘로부터 20년 전 날짜 (예: 2005-12-30)
        LocalDate limitDate = today.minusYears(20);

        // 생년월일이 제한일보다 이전(isBefore)이거나 같아야(isEqual) 함
        return birth.isBefore(limitDate) || birth.equals(limitDate);
    }
}