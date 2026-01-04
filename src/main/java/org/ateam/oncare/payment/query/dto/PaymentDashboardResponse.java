package org.ateam.oncare.payment.query.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDashboardResponse {
    private int myPendingCount;     // 나의 결재 대기 (내가 승인해야 할 문서)
    private int myDraftPendingCount;// 승인 대기중 (내가 올린 결재)
    private int myApprovedCount;    // 승인됨
    private int myRejectedCount;    // 반려됨
}