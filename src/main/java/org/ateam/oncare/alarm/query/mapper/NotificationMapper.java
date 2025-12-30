package org.ateam.oncare.alarm.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.alarm.query.dto.NotificationQueryDTO;

import java.util.List;

@Mapper
public interface NotificationMapper {
    // 알림 목록 조회 (템플릿, 이벤트타입 조인)
    List<NotificationQueryDTO> selectAlarmList(@Param("receiverId") Long receiverId);

    // 안 읽은 알림 개수
    int countUnread(@Param("receiverId") Long receiverId);
}