package org.ateam.oncare.beneficiary.command.controller;

import org.ateam.oncare.alarm.command.dto.RedisMessageOfNotificationDTO;
import org.ateam.oncare.alarm.query.dto.NotificationQueryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface NotificationQueryMapstruct {
    RedisMessageOfNotificationDTO toMessageForRedisDTO(NotificationQueryDTO notyDTO);
    NotificationQueryDTO toNotyDTO(RedisMessageOfNotificationDTO redisMessageDTO);
}
