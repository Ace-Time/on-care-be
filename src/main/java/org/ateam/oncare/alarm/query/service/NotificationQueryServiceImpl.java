package org.ateam.oncare.alarm.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.query.dto.NotificationQueryDTO;
import org.ateam.oncare.alarm.query.mapper.NotificationMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회 전용 최적화
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationQueryDTO> getMyAlarms(Long userId) {
        return notificationMapper.selectAlarmList(userId);
    }

    @Override
    public int getUnreadCount(Long userId) {
        return notificationMapper.countUnread(userId);
    }
}