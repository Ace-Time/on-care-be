package org.ateam.oncare.alarm.command.repository;

import org.ateam.oncare.alarm.command.entity.NotificationEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationEventTypeRepository extends JpaRepository<NotificationEventType, Long> {
}