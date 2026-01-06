package org.ateam.oncare.beneficiary.command.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.alarm.command.dto.MessageType;
import org.ateam.oncare.alarm.command.dto.RedisMessageOfNotificationDTO;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.alarm.query.dto.NotificationQueryDTO;
import org.ateam.oncare.beneficiary.command.controller.NotificationQueryMapstruct;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationSubscriberForRedis {
    private final ObjectMapper objectMapper;
    private final NotificationCommandService notyService;
    private final NotificationQueryMapstruct notificationQueryMapstruct;

    /**
     * redis 메시지 도착시 실행되는 메소드
     * @param message
     */
    public void  notificationsToEmployee(String message) {
        try{
            RedisMessageOfNotificationDTO dto = objectMapper.readValue(message, RedisMessageOfNotificationDTO.class);
            log.info("[Pub/Sub 수신] {}", dto);

            NotificationQueryDTO requestMessage = notificationQueryMapstruct.toNotyDTO(dto);
            notyService.sendToClient(dto.getReceiverId(),requestMessage);
        } catch( Exception e){
            log.error("[notificationsToEmployee]Redis 메시지 형식 오류 {}", e.getMessage());
        }
    }
}
