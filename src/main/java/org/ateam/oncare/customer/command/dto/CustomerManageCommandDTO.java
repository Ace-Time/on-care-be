package org.ateam.oncare.customer.command.dto;

import lombok.*;

import java.time.LocalDate;

public class CustomerManageCommandDTO {

    /**
     * 후속조치 요청
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowUpRequest {
        private Long beneficiaryId;
        private Long counselId;
        private String followUpContent;
        private String actionType;  // COUNSEL_REQUEST, COMPLAINT_RESOLVE, TERMINATION_FOLLOWUP, RENTAL_NOTIFY
    }

    /**
     * 해지 등록 요청
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TerminationRequest {
        private Long beneficiaryId;
        private LocalDate plannedTerminationDate;
        private String terminationReason;
    }

    /**
     * 알림 발송 요청
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationRequest {
        private Long beneficiaryId;
        private Integer receiverId;      // 수신자 ID (요양보호사 또는 상담사)
        private String notificationType; // COUNSEL_REQUEST, COMPLAINT_FOLLOWUP, TERMINATION_15DAYS, TERMINATION_3DAYS, RENTAL_NOTIFY
        private String customMessage;
    }

    /**
     * 일반 응답
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommandResponse {
        private boolean success;
        private String message;
        private Object data;
    }
}
