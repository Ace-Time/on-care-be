package org.ateam.oncare.payment.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentListResponse {
    private Long id;
    private String categoryName;    // 결재 유형 (급여, 구매 등)
    private String title;           // 제목 (DDL엔 없으나 UI에 필수)
    private String drafterName;     // 기안자 이름
    private String priority;        // 긴급, 보통
    private String status;          // 대기중, 승인, 반려
    private LocalDateTime createdAt;// 기안일
    private Long amount;            // 금액 (UI상 존재)
}