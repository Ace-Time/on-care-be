package org.ateam.oncare.alarm.command.service;

import org.ateam.oncare.alarm.command.entity.NotificationLog;
import org.ateam.oncare.alarm.command.entity.NotificationTemplate;
import org.ateam.oncare.alarm.command.repository.NotificationLogRepository;
import org.ateam.oncare.alarm.command.repository.NotificationTemplateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationCommandServiceTest {

    @Mock
    private NotificationLogRepository logRepository;
    @Mock
    private NotificationTemplateRepository templateRepository;
    @Mock
    private org.ateam.oncare.employee.command.repository.EmployeeRepository employeeRepository;
    @Mock
    private org.ateam.oncare.alarm.command.repository.NotificationEventTypeRepository eventTypeRepository;
    @Mock
    private org.ateam.oncare.beneficiary.command.service.NotificationPublisherForRedis publisherForRedis;

    @InjectMocks
    private NotificationCommandServiceImpl notificationService;

    @Test
    @DisplayName("알림 템플릿을 이용한 전송 및 로그 저장 테스트")
    void testSendNotificationWithTemplate() {
        // Given
        Long receiverId = 101L;
        Long templateId = 5L;

        NotificationTemplate mockTemplate = new NotificationTemplate();
        mockTemplate.setTitle("템플릿 알림");
        mockTemplate.setContent("템플릿 내용");
        mockTemplate.setSeverity(1);
        mockTemplate.setTemplateType("INFO");

        when(templateRepository.findById(templateId)).thenReturn(Optional.of(mockTemplate));
        when(logRepository.save(any(NotificationLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        notificationService.send(receiverId, templateId);

        // Then
        verify(logRepository, times(1)).save(any(NotificationLog.class));
    }

    @Test
    @DisplayName("SSE 구독 테스트")
    void testSubscribe() {
        // Given
        Long userId = 1L;

        // When
        org.springframework.web.servlet.mvc.method.annotation.SseEmitter emitter = notificationService
                .subscribe(userId);

        // Then
        org.junit.jupiter.api.Assertions.assertNotNull(emitter);
        // Timeout check (optional)
        assertEquals(60L * 1000 * 60, emitter.getTimeout());
    }

    @Test
    @DisplayName("알림 삭제 테스트")
    void testDeleteNotification() {
        // Given
        Long alarmId = 123L;
        when(logRepository.existsById(alarmId)).thenReturn(true);

        // When
        notificationService.deleteNotification(alarmId);

        // Then
        verify(logRepository, times(1)).deleteById(alarmId);
    }

    @Test
    @DisplayName("직무 코드별 다중 발송 테스트")
    void testSendByJobCode() {
        // Given
        Long jobCode = 5L;
        Long templateId = 1L;

        org.ateam.oncare.employee.command.entity.Employee emp1 = new org.ateam.oncare.employee.command.entity.Employee();
        emp1.setId(10);
        org.ateam.oncare.employee.command.entity.Employee emp2 = new org.ateam.oncare.employee.command.entity.Employee();
        emp2.setId(11);

        when(employeeRepository.findByJobCode(jobCode)).thenReturn(java.util.List.of(emp1, emp2));

        NotificationTemplate mockTemplate = new NotificationTemplate();
        mockTemplate.setTitle("공지");
        mockTemplate.setContent("내용");
        when(templateRepository.findById(templateId)).thenReturn(Optional.of(mockTemplate));

        // When
        notificationService.sendByJobCode(jobCode, templateId);

        // Then
        // 2명의 직원에게 각각 save가 호출되어야 함
        verify(logRepository, times(2)).save(any(NotificationLog.class));
    }

    @Test
    @DisplayName("커스텀 알림 전송 및 로그 저장 테스트")
    void testSendCustomNotification() {
        // Given
        Long receiverId = 102L;
        String title = "직접 보낸 알림";
        String content = "내용입니다.";
        String templateType = "WARN";
        Integer severity = 2;

        when(logRepository.save(any(NotificationLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        notificationService.sendCustom(receiverId, title, content, templateType, severity);

        // Then
        verify(logRepository, times(1)).save(any(NotificationLog.class));
    }
}
