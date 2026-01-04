package org.ateam.oncare.customer.query.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.customer.query.mapper.CustomerManageMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TerminationNotificationScheduler {

    private final NotificationCommandService notificationCommandService;
    private final CustomerManageMapper customerManageMapper;

    private static final int SEVERITY_URGENT = 1;
    private static final int SEVERITY_IMPORTANT = 2;

    /**
     * 매일 오전 9시에 해지 예정 알림 발송
     * - 15일 전: 상담사에게 알림
     * - 3일 전: 담당 요양보호사에게 알림
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendTerminationNotifications() {
        log.info("=== 해지 예정 알림 스케줄러 시작 ===");

        LocalDate today = LocalDate.now();
        LocalDate fifteenDaysLater = today.plusDays(15);
        LocalDate threeDaysLater = today.plusDays(3);

        // 15일 후 해지 예정 고객 조회 및 상담사 알림
        sendNotificationsForDate(fifteenDaysLater, 15, "상담사");

        // 3일 후 해지 예정 고객 조회 및 요양보호사 알림
        sendNotificationsForDate(threeDaysLater, 3, "요양보호사");

        log.info("=== 해지 예정 알림 스케줄러 완료 ===");
    }

    private void sendNotificationsForDate(LocalDate targetDate, int daysBefore, String receiverType) {
        try {
            // 해당 날짜에 해지 예정인 수급자 목록 조회
            List<Map<String, Object>> beneficiaries = findBeneficiariesByTerminationDate(targetDate);

            for (Map<String, Object> beneficiary : beneficiaries) {
                Long beneficiaryId = ((Number) beneficiary.get("beneficiaryId")).longValue();
                String beneficiaryName = (String) beneficiary.get("name");

                Integer receiverId;
                if (daysBefore == 15) {
                    // 15일 전: 마지막 상담한 상담사에게 알림
                    receiverId = getLastCounselorId(beneficiaryId);
                } else {
                    // 3일 전: 담당 요양보호사에게 알림
                    receiverId = customerManageMapper.selectCareWorkerId(beneficiaryId);
                }

                if (receiverId != null) {
                    sendTerminationAlertNotification(beneficiaryId, beneficiaryName, receiverId, daysBefore, receiverType);
                }
            }

            log.info("{}일 전 해지 예정 알림 발송 완료: {}건", daysBefore, beneficiaries.size());

        } catch (Exception e) {
            log.error("{}일 전 해지 예정 알림 발송 실패", daysBefore, e);
        }
    }

    private void sendTerminationAlertNotification(Long beneficiaryId, String beneficiaryName,
                                                  Integer receiverId, int daysBefore, String receiverType) {
        try {
            String title = String.format("[해지예정] %d일 전 알림", daysBefore);
            String content = String.format(
                    "수급자 '%s'님의 서비스 해지 예정일이 %d일 남았습니다.\n" +
                            "고객 이탈 방지를 위한 후속 상담을 진행해 주세요.",
                    beneficiaryName,
                    daysBefore
            );

            notificationCommandService.sendCustom(
                    Long.valueOf(receiverId),
                    title,
                    content,
                    "해지예정",
                    daysBefore == 3 ? SEVERITY_URGENT : SEVERITY_IMPORTANT
            );

            log.info("해지 {}일 전 알림 발송 - beneficiaryId: {}, receiverId: {}, receiverType: {}",
                    daysBefore, beneficiaryId, receiverId, receiverType);

        } catch (Exception e) {
            log.error("해지 알림 발송 실패 - beneficiaryId: {}", beneficiaryId, e);
        }
    }

    /**
     * 특정 날짜에 해지 예정인 수급자 목록 조회
     * (실제 구현 시 Mapper 메서드 추가 필요)
     */
    private List<Map<String, Object>> findBeneficiariesByTerminationDate(LocalDate date) {
        // TODO: CustomerManageMapper에 해당 메서드 추가 필요
        // 현재는 빈 리스트 반환
        return List.of();
    }

    /**
     * 수급자의 마지막 상담사 ID 조회
     * (실제 구현 시 Mapper 메서드 추가 필요)
     */
    private Integer getLastCounselorId(Long beneficiaryId) {
        // TODO: CustomerManageMapper에 해당 메서드 추가 필요
        return null;
    }
}
