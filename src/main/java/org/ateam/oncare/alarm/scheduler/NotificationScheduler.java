package org.ateam.oncare.alarm.scheduler;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.command.entity.NotificationRule;
import org.ateam.oncare.alarm.command.repository.NotificationRuleRepository;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.careworker.command.entity.CareWorkerCertificate;
import org.ateam.oncare.careworker.command.entity.Education;
import org.ateam.oncare.employee.command.repository.CareWorkerCertificateRepository;
import org.ateam.oncare.employee.command.repository.EducationRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationRuleRepository ruleRepository;
    private final NotificationCommandService notificationService;
    private final EducationRepository educationRepository;
    private final CareWorkerCertificateRepository certificateRepository;

    /**
     * 매일 오전 9시에 실행되어, 보수교육 만료 임박 대상자에게 알림 발송
     */
    @Scheduled(cron = "0 0 9 * * ?") // 매일 09:00:00 실행
    @Transactional
    public void checkEducationExpiration() {
        // 1. 활성화된 모든 알림 규칙 조회
        List<NotificationRule> activeRules = ruleRepository.findByIsActive(1);

        for (NotificationRule rule : activeRules) {
            // 주의: 현재 NotificationRule에는 "이 규칙이 보수교육용이다"라는 명시적 구분 컬럼이 없습니다.
            // 따라서 실제 운영 시에는 rule의 templateId나 별도 컬럼을 확인하여
            // "보수교육 관련 규칙"인 경우에만 아래 로직을 실행하도록 필터링해야 합니다.
            // 여기서는 예시로 모든 활성 규칙에 대해 "offsetDays" 만큼 남은 보수교육을 검색합니다.

            // 2. 알림 보낼 날짜 계산 (예: 만료일 - 7일 = 오늘 => 만료일 = 오늘 + 7일)
            // offsetDays가 null이면 기본값(예: 7일) 사용하거나 건너뜀
            if (rule.getOffsetDays() == null)
                continue;

            LocalDate targetDate = LocalDate.now().plusDays(rule.getOffsetDays());

            // 3. 해당 날짜가 '차기 교육일(만료일)'인 교육 데이터 조회
            List<Education> educations = educationRepository.findByNextEduDate(targetDate);

            if (educations.isEmpty())
                continue;

            // 4. 대상 직원 ID 추출
            // Education -> CareWorkerCertificate -> CareWorker -> EmployeeId
            List<Long> certIds = educations.stream()
                    .map(Education::getCareWorkerCertificateId)
                    .collect(Collectors.toList());

            if (certIds.isEmpty())
                continue;

            List<CareWorkerCertificate> certs = certificateRepository.findAllById(certIds);

            List<Long> receiverIds = certs.stream()
                    .map(cert -> cert.getCareWorker().getEmployeeId())
                    .distinct() // 중복 제거
                    .collect(Collectors.toList());

            // 5. 알림 발송
            if (!receiverIds.isEmpty()) {
                System.out.println(
                        "[Scheduler] 보수교육 만료 알림 발송: " + receiverIds.size() + "명, 템플릿ID: " + rule.getTemplateId());
                notificationService.send(receiverIds, rule.getTemplateId());
            }
        }
    }
}
