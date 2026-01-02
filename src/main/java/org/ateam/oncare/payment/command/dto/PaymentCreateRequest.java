package org.ateam.oncare.payment.command.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentCreateRequest {
    private Long categoryId;
    private String title;
    private String content;
    private Integer priority; // 0:긴급, 1:보통, 2:낮음

    // 선택 항목
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;

    // 결재선 (결재자 ID 목록, 순서대로)
    private List<Long> approverIds;
}
