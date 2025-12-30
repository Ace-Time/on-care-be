package org.ateam.oncare.employee.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignedBeneficiaryDTO {

    // 매칭 정보 (수정/취소 시 필요)
    private Long matchingId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // 'Y' 등

    // 수급자 정보 (이름, 생년월일 등 표시용)
    private Long beneficiaryId;
    private String name;
    private String birthDate;
    private String gender;

    // 담당 요양보호사(직원) 이름
    private String employeeName;
}