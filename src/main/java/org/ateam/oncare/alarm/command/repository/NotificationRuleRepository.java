package org.ateam.oncare.alarm.command.repository;

import org.ateam.oncare.alarm.command.entity.NotificationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRuleRepository extends JpaRepository<NotificationRule, Long> {
    // 활성화된 알림 규칙 조회
    List<NotificationRule> findByIsActive(Integer isActive);

    List<NotificationRule> findByTemplateIdAndIsActive(Long templateId, Integer isActive);
}
