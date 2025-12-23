package org.ateam.oncare.alarm.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.command.dto.NotificationRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/alarm") // 테스트용 경로
@RequiredArgsConstructor
public class NotificationTestController {

    private final org.ateam.oncare.alarm.command.service.NotificationCommandService notificationCommandService;

    // 팀원이 "알림 보내줘!" 라고 요청하는 상황을 시뮬레이션
    @PostMapping("/send")
    public ResponseEntity<String> testSendNotification(@RequestBody NotificationRequest request) {

        // 팀원들이 실제 코드에서 호출할 메서드 (CommandService로 통합됨)
        notificationCommandService.send(request);

        return ResponseEntity.ok("알림 요청이 성공적으로 접수되었습니다.");
    }
}