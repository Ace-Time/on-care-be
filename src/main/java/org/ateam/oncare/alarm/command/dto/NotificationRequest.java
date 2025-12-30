package org.ateam.oncare.alarm.command.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private Long receiverId; // 받는 사람 ID
    private Long templateId; // 템플릿 번호 (1:결재, 2:공지 등)
}
