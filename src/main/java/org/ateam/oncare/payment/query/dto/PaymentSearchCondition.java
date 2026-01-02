package org.ateam.oncare.payment.query.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentSearchCondition {
    private Long employeeId; // 로그인한 사용자 ID
    private String keyword; // 제목 또는 요청자 검색
    private String category; // 전체 유형 필터
    private String status; // 전체 상태 필터

    private Integer page = 0; // 페이지 번호 (기본 0, 0-based index)
    private Integer size = 10; // 페이지 당 개수 (기본 10)

    public int getOffset() {
        return Math.max(0, page) * size;
    }
}