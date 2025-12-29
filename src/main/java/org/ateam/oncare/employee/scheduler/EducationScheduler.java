package org.ateam.oncare.employee.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.employee.query.dto.EducationAlertDTO;
import org.ateam.oncare.employee.query.mapper.CertAndEduQueryMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EducationScheduler {

    private final CertAndEduQueryMapper certAndEduQueryMapper;
    private final NotificationCommandService notificationService;
    private final org.ateam.oncare.alarm.command.repository.NotificationTemplateRepository templateRepository;
    private final org.ateam.oncare.alarm.command.repository.NotificationRuleRepository ruleRepository;

    // 매일 오전 9시에 실행
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void checkEducationStatus() {
        log.info("보수교육/자격증 갱신 대상자 확인 중...");

        // 1. 'EDU_REQUIRED' 타입의 활성 템플릿 조회
        List<org.ateam.oncare.alarm.command.entity.NotificationTemplate> templates = templateRepository
                .findByTemplateTypeAndIsActive("EDU_REQUIRED", 1);

        if (templates.isEmpty()) {
            log.info("활성화된 'EDU_REQUIRED' 알림 템플릿이 없습니다. (DB 조회 결과 0건)");

            // [디버깅] 전체 템플릿 조회하여 로그 출력
            List<org.ateam.oncare.alarm.command.entity.NotificationTemplate> all = templateRepository.findAll();
            log.info("--- 현재 저장된 템플릿 목록 (총 {}건) ---", all.size());
            for (org.ateam.oncare.alarm.command.entity.NotificationTemplate t : all) {
                log.info("ID: {}, Type: '{}', Active: {}", t.getTemplateId(), t.getTemplateType(), t.getIsActive());
            }
            log.info("-------------------------------------------");
            return;
        }

        // 2. 각 템플릿에 연결된 규칙 조회 및 실행
        for (org.ateam.oncare.alarm.command.entity.NotificationTemplate template : templates) {
            List<org.ateam.oncare.alarm.command.entity.NotificationRule> rules = ruleRepository
                    .findByTemplateIdAndIsActive(template.getTemplateId(), 1);

            for (org.ateam.oncare.alarm.command.entity.NotificationRule rule : rules) {
                processRule(template, rule);
            }
        }
    }

    private void processRule(org.ateam.oncare.alarm.command.entity.NotificationTemplate template,
            org.ateam.oncare.alarm.command.entity.NotificationRule rule) {

        int offsetDays = rule.getOffsetDays() != null ? rule.getOffsetDays() : 14;

        // offsetDays(예: 14)일 후를 기준일로 설정
        // 오늘로부터 14일 뒤(threshold)보다 이전에 교육 받아야 하는 사람 = 14일 이내로 남았거나 지난 사람
        LocalDate thresholdDate = LocalDate.now().plusDays(offsetDays);

        List<EducationAlertDTO> alerts = certAndEduQueryMapper.selectEducationAlerts(thresholdDate);

        for (EducationAlertDTO alert : alerts) {
            // 내용 치환 (예: "{name}님의 교육이 {dDay}일 {statusMsg}")
            String content = template.getContent();
            String statusMsg = "OVERDUE".equals(alert.getStatus()) ? "지났습니다" : "남았습니다";

            content = content.replace("{name}", alert.getName())
                    .replace("{date}", alert.getNextEduDate().toString())
                    .replace("{dDay}", String.valueOf(Math.abs(alert.getDDay())))
                    .replace("{statusMsg}", statusMsg);

            // 알림 발송 (커스텀 전송 사용)
            try {
                notificationService.sendCustom(
                        alert.getEmployeeId(),
                        template.getTitle(),
                        content,
                        template.getTemplateType(),
                        template.getSeverity() != null ? template.getSeverity() : 2);
            } catch (Exception e) {
                log.error("알림 발송 실패 (EmployeeID: {})", alert.getEmployeeId(), e);
            }
        }
        log.info("규칙(ID:{})에 따른 알림 발송 완료: {}건", rule.getRuleId(), alerts.size());
    }
}
