package org.ateam.oncare.alarm.command.repository;

import org.ateam.oncare.alarm.command.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    // 기본 CRUD(save, findById, delete 등)는 JpaRepository가 자동으로 제공합니다.
    // 복잡한 조회는 Query 패키지의 MyBatis Mapper가 담당하므로 추가 메서드가 필요 없습니다.

    java.util.List<NotificationLog> findAllByReceiverIdAndStatus(Long receiverId,
            org.ateam.oncare.alarm.command.entity.NotificationStatus status);
}