package org.ateam.oncare.employee.command.dto;


import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCertificateDTO {
    private Long certificateId;   // 자격증 마스터 ID (예: 1=요양보호사)
    private String licenseNo;

    // 1. 발급일은 과거 또는 오늘이어야 함
    @PastOrPresent(message = "자격증 발급일은 미래일 수 없습니다.")
    private LocalDate issueDate;

    private LocalDate expireDate;

    // 2. 커스텀 검증 로직: 만료일이 발급일보다 뒤인지 확인
    @AssertTrue(message = "자격증 만료일은 발급일보다 이후여야 합니다.")
    public boolean isDateRangeValid() {
        // 날짜가 하나라도 비어있으면 이 검증은 통과시킴 (NotNull 검사는 따로 있다고 가정)
        if (issueDate == null || expireDate == null) {
            return true;
        }
        // expireDate가 issueDate보다 뒤(after)이거나 같아야 함
        return expireDate.isAfter(issueDate) || expireDate.equals(issueDate);
    }
}
