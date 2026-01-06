package org.ateam.oncare.beneficiary.command.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.alarm.command.dto.RedisMessageOfNotificationDTO;
import org.ateam.oncare.alarm.query.dto.NotificationQueryDTO;
import org.ateam.oncare.beneficiary.command.controller.NotificationQueryMapstruct;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisherForRedis {
    private final StringRedisTemplate stringRedisTemplate; // String 전용 템플릿 주입
    private final ChannelTopic channelTopic;
    private final ObjectMapper objectMapper;
    private final NotificationQueryMapstruct notificationQueryMapstruct;

    public void publish(Long userId, NotificationQueryDTO data) {
        try {

            RedisMessageOfNotificationDTO responseDTO = notificationQueryMapstruct.toMessageForRedisDTO(data);
            responseDTO.setReceiverId(userId);

            // DTO -> JSON String 변환
            String jsonMessage = objectMapper.writeValueAsString(responseDTO);

            // Redis로 문자열 발행
            stringRedisTemplate.convertAndSend(channelTopic.getTopic(), jsonMessage);

        } catch (JsonProcessingException e) {
            log.error("이벤트 발행 실패: JSON 변환 오류");
        }
    }
}
