package org.ateam.oncare.alarm.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
public class NotificationCommandController {

    private final NotificationCommandService NotificationcommandService;

    // 1. SSE 구독 (로그인 직후 호출) - 서버의 메모리 상태를 변경하므로 Command에 배치
    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long userId) {
        return NotificationcommandService.subscribe(userId);
    }

    // 2. 알림 읽음 처리 (수정)
    @PatchMapping("/{alarmId}/read")
    public ResponseEntity<Void> readNotification(@PathVariable Long alarmId) {
        NotificationcommandService.readNotification(alarmId);
        return ResponseEntity.ok().build();
    }

    // 2-1. 모든 알림 읽음 처리 (일괄)
    @PatchMapping("/read-all/{userId}")
    public ResponseEntity<Void> readAllNotifications(@PathVariable Long userId) {
        NotificationcommandService.readAllNotifications(userId);
        return ResponseEntity.ok().build();
    }

    // 3. 알림 삭제 (DB 삭제)
    @DeleteMapping("/{alarmId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long alarmId) {
        NotificationcommandService.deleteNotification(alarmId);
        return ResponseEntity.ok().build();
    }

    // (테스트용) 임의 알림 발송 API
    // 실제로는 다른 서비스(결재 등)에서 Service 메서드를 직접 호출하겠지만, 테스트를 위해 둠
    @PostMapping("/send/{receiverId}")
    public ResponseEntity<Void> testSend(@PathVariable Long receiverId, @RequestParam Long templateId) {
        NotificationcommandService.send(receiverId, templateId);
        return ResponseEntity.ok().build();
    }
}