package org.ateam.oncare.alarm.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.query.dto.NotificationQueryDTO;
import org.ateam.oncare.alarm.query.service.NotificationQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
public class NotificationQueryController {

    private final NotificationQueryService NotificationqueryService;

    // 알림 목록 조회 (종 모양 클릭 시)
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationQueryDTO>> getAlarms(@PathVariable Long userId) {
        return ResponseEntity.ok(NotificationqueryService.getMyAlarms(userId));
    }

    // 안 읽은 개수 조회 (배지 표시용)
    @GetMapping("/{userId}/unread-count")
    public ResponseEntity<Integer> getUnreadCount(@PathVariable Long userId) {
        return ResponseEntity.ok(NotificationqueryService.getUnreadCount(userId));
    }
}