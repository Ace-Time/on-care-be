package org.ateam.oncare.employee.command.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EducationInputDTO {
    private String eduName;
    private String institution;

    // 1. 교육일은 과거 또는 오늘이어야 함
    @PastOrPresent(message = "교육 이수일은 미래일 수 없습니다.")
    private LocalDate eduDate;

    private LocalDate nextEduDate;
    private Boolean isOverdue;
    private Integer status; // 0:대기, 2:승인 등

    // 2. 커스텀 검증 로직: 다음 교육일이 교육일보다 뒤인지 확인
    @AssertTrue(message = "다음 교육일은 교육 이수일보다 이후여야 합니다.")
    public boolean isEducationDateRangeValid() {
        // 날짜가 하나라도 비어있으면 이 검증은 통과시킴
        if (eduDate == null || nextEduDate == null) {
            return true;
        }
        // nextEduDate가 eduDate보다 뒤(after)여야 함
        return nextEduDate.isAfter(eduDate);
    }
}
