package org.ateam.oncare.alarm.query.service;

import org.ateam.oncare.alarm.query.dto.NotificationQueryDTO;

import java.util.List;

public interface NotificationQueryService {

    // 내 알림 목록 조회
    List<NotificationQueryDTO> getMyAlarms(Long userId);

    // 안 읽은 개수 조회
    int getUnreadCount(Long userId);
}