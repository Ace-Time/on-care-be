package org.ateam.oncare.alarm.command.repository;

import org.ateam.oncare.alarm.command.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
}