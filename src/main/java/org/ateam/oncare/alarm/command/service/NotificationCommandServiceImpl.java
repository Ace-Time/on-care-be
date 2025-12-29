package org.ateam.oncare.alarm.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.command.entity.*;
import org.ateam.oncare.alarm.command.repository.*;
import org.ateam.oncare.alarm.query.dto.NotificationQueryDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationLogRepository logRepository;
    private final NotificationTemplateRepository templateRepository;
    private final NotificationEventTypeRepository eventTypeRepository;
    private final org.ateam.oncare.employee.command.repository.EmployeeRepository employeeRepository; // 직업 코드로 직원을 찾기
                                                                                                      // 위해 주입

    // SSE 저장소 (메모리)
    private static final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(60L * 1000 * 60); // 1시간
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((e) -> emitters.remove(userId));

        try {
            emitter.send(SseEmitter.event().name("connect").data("Connected!"));
        } catch (IOException e) {
            emitters.remove(userId);
        }
        return emitter;
    }

    @Override
    public void send(Long receiverId, Long templateId) {
        // 1. 템플릿 조회 (내용 복사용)
        NotificationTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Template ID"));

        // 2. 로그 객체 생성 (Builder 패턴 사용)
        NotificationLog log = NotificationLog.builder()
                .receiverId(receiverId)
                .receiverType(ReceiverType.EMPLOYEE) // 받는 사람이 직원이면 EMPLOYEE

                // [데이터 복사] 템플릿 -> 로그
                .title(template.getTitle()) // 제목 복사
                .content(template.getContent()) // 내용 복사
                .templateType(template.getTemplateType()) // 유형 복사
                .severity(template.getSeverity()) // 중요도(severity) 복사 (User snippet had getTargetTypeId, corrected to
                                                  // getSeverity based on Entity)

                // [신규 컬럼] 타겟 타입 (템플릿에 있는 값을 가져오거나, 기본값 설정)
                .targetType(TargetType.EMPLOYEE)

                .sentAt(java.time.LocalDateTime.now())
                .status(NotificationStatus.SENT) // 기본 상태
                .build();

        // 3. 저장 (참조 제약 없이 독립적으로 저장됨)
        logRepository.save(log);

        // 4. 실시간 전송 (SSE)
        // DTO static factory method 사용
        NotificationQueryDTO liveData = NotificationQueryDTO.from(log);

        sendToClient(receiverId, liveData);
    }

    @Override
    public void readNotification(Long alarmId) {
        NotificationLog log = logRepository.findById(alarmId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Alarm ID"));

        log.markAsRead(); // 편의 메서드 사용
        // Dirty Checking으로 자동 update
    }

    @Override
    public void deleteNotification(Long alarmId) {
        if (!logRepository.existsById(alarmId)) {
            throw new IllegalArgumentException("Invalid Alarm ID");
        }
        logRepository.deleteById(alarmId);
    }

    @Override
    public void send(org.ateam.oncare.alarm.command.dto.NotificationRequest request) {
        this.send(request.getReceiverId(), request.getTemplateId());
    }

    // 다중 발송 (List)
    @Override
    public void send(java.util.List<Long> receiverIds, Long templateId) {
        // 리스트가 비어있으면 종료
        if (receiverIds == null || receiverIds.isEmpty()) {
            return;
        }

        // 반복문으로 하나씩 발송
        for (Long id : receiverIds) {
            this.send(id, templateId);
        }
    }

    // 직업 코드로 여러 명에게 보내기
    @Override
    public void sendByJobCode(Long jobCode, Long templateId) {
        // 1. 해당 직무를 가진 모든 직원 조회
        java.util.List<org.ateam.oncare.employee.command.entity.Employee> employees = employeeRepository
                .findByJobCode(jobCode);

        // 2. 반복문으로 기존 send 메서드 호출
        for (org.ateam.oncare.employee.command.entity.Employee emp : employees) {
            // Employee ID가 Integer라면 Long으로 변환
            this.send(Long.valueOf(emp.getId()), templateId);
        }
    }

    @Override
    public void readAllNotifications(Long receiverId) {
        // 해당 사용자의 'SENT' 상태 알림을 모두 조회하여 'READ'로 변경
        java.util.List<NotificationLog> logs = logRepository.findAllByReceiverIdAndStatus(receiverId,
                NotificationStatus.SENT);

        for (NotificationLog log : logs) {
            log.markAsRead();
        }
    }

    private void sendToClient(Long userId, Object data) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(data));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        }
    }

    @Override
    public void sendCustom(Long receiverId, String title, String content, String templateType, Integer severity) {
        NotificationLog log = NotificationLog.builder()
                .receiverId(receiverId)
                .receiverType(ReceiverType.EMPLOYEE)
                .title(title)
                .content(content)
                .templateType(templateType)
                .severity(severity)
                .targetType(TargetType.EMPLOYEE)
                .sentAt(java.time.LocalDateTime.now())
                .status(NotificationStatus.SENT)
                .build();

        logRepository.save(log);

        NotificationQueryDTO liveData = NotificationQueryDTO.from(log);
        sendToClient(receiverId, liveData);
    }
}