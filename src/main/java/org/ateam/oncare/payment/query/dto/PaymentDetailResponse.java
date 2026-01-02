package org.ateam.oncare.payment.query.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDetailResponse {
    private Long id;
    private String categoryName;
    private String title;
    private String content;         // 결재 내용
    private String drafterName;
    private LocalDateTime createdAt;
    private String priority;
    private String status;
    private Long amount;

    // 결재 라인 정보 (팀장 김팀장 등)
    private List<ApproverInfo> approverList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ApproverInfo {
        private String approverName;
        private String stepStatus; // 대기중, 승인 등
    }
}