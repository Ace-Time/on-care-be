package org.ateam.oncare.alarm.command.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationCommandService {

    // SSE 구독 요청
    SseEmitter subscribe(Long userId);

    // 알림 생성 및 전송
    void send(Long receiverId, Long templateId);

    // DTO를 이용한 알림 전송 (통합됨)
    void send(org.ateam.oncare.alarm.command.dto.NotificationRequest request);

    // 다중 발송 (여러 명의 ID 리스트)
    void send(java.util.List<Long> receiverIds, Long templateId);

    // 직업 코드로 다중 발송
    void sendByJobCode(Long jobCode, Long templateId);

    // 알림 읽음 처리
    void readNotification(Long alarmId);

    // 알림 삭제
    void deleteNotification(Long alarmId);

    // 모든 알림 읽음 처리
    void readAllNotifications(Long receiverId);
}