package org.ateam.oncare.customer.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.beneficiary.command.entity.BeneficiaryHistory;
import org.ateam.oncare.beneficiary.command.repository.BeneficiaryHistoryRepository;
import org.ateam.oncare.counsel.command.entity.CounselHistory;
import org.ateam.oncare.counsel.command.repository.CounselHistoryRepository;
import org.ateam.oncare.customer.command.dto.CustomerManageCommandDTO;
import org.ateam.oncare.customer.query.dto.CustomerManageDTO;
import org.ateam.oncare.customer.query.mapper.CustomerManageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerManageCommandService {

    private final NotificationCommandService notificationCommandService;
    private final CounselHistoryRepository counselHistoryRepository;
    private final BeneficiaryHistoryRepository beneficiaryHistoryRepository;
    private final CustomerManageMapper customerManageMapper;

    // 알림 중요도 상수
    private static final int SEVERITY_URGENT = 1;    // 긴급
    private static final int SEVERITY_IMPORTANT = 2; // 중요
    private static final int SEVERITY_NORMAL = 3;    // 일반

    /**
     * 이탈위험 - 상담 요청 알림 발송
     * 30일 이상 미상담 시 담당 상담사에게 알림
     */
    public CustomerManageCommandDTO.CommandResponse sendChurnRiskNotification(Long beneficiaryId, Integer counselorId) {
        try {
            CustomerManageDTO.CustomerManageDetail detail = customerManageMapper.selectCustomerManageDetail(beneficiaryId);

            String title = "[이탈위험] 상담 요청";
            String content = String.format(
                    "수급자 '%s'님이 %d일간 상담 이력이 없습니다. 상담을 진행해 주세요.",
                    detail.getName(),
                    detail.getDaysSinceLastCounsel() != null ? detail.getDaysSinceLastCounsel() : 30
            );

            notificationCommandService.sendCustom(
                    Long.valueOf(counselorId),
                    title,
                    content,
                    "이탈위험",
                    SEVERITY_IMPORTANT
            );

            log.info("이탈위험 알림 발송 완료 - beneficiaryId: {}, counselorId: {}", beneficiaryId, counselorId);

            return CustomerManageCommandDTO.CommandResponse.builder()
                    .success(true)
                    .message("상담 요청 알림이 발송되었습니다.")
                    .build();

        } catch (Exception e) {
            log.error("이탈위험 알림 발송 실패", e);
            return CustomerManageCommandDTO.CommandResponse.builder()
                    .success(false)
                    .message("알림 발송에 실패했습니다: " + e.getMessage())
                    .build();
        }
    }

    /**
     * 불만상담 - 후속조치 알림 발송
     */
    public CustomerManageCommandDTO.CommandResponse sendComplaintFollowUpNotification(CustomerManageCommandDTO.FollowUpRequest request, Integer counselorId) {
        try {
            // 상담 이력에 후속조치 내용 업데이트
            if (request.getCounselId() != null) {
                CounselHistory counsel = counselHistoryRepository.findById(request.getCounselId())
                        .orElseThrow(() -> new IllegalArgumentException("상담 이력을 찾을 수 없습니다."));
                counsel.setFollowUp(request.getFollowUpContent());
                counsel.setFollowUpNecessary("Y");
                counselHistoryRepository.save(counsel);
            }

            CustomerManageDTO.CustomerManageDetail detail = customerManageMapper.selectCustomerManageDetail(request.getBeneficiaryId());

            String title = "[불만상담] 후속조치 등록";
            String content = String.format(
                    "수급자 '%s'님의 불만상담에 대한 후속조치가 등록되었습니다.\n\n후속조치: %s",
                    detail.getName(),
                    request.getFollowUpContent()
            );

            // 담당 요양보호사에게 알림
            Integer careWorkerId = customerManageMapper.selectCareWorkerId(request.getBeneficiaryId());
            if (careWorkerId != null) {
                notificationCommandService.sendCustom(
                        Long.valueOf(careWorkerId),
                        title,
                        content,
                        "불만상담",
                        SEVERITY_IMPORTANT
                );
            }

            log.info("불만상담 후속조치 알림 발송 완료 - beneficiaryId: {}", request.getBeneficiaryId());

            return CustomerManageCommandDTO.CommandResponse.builder()
                    .success(true)
                    .message("후속조치가 등록되고 알림이 발송되었습니다.")
                    .build();

        } catch (Exception e) {
            log.error("불만상담 후속조치 알림 발송 실패", e);
            return CustomerManageCommandDTO.CommandResponse.builder()
                    .success(false)
                    .message("후속조치 등록에 실패했습니다: " + e.getMessage())
                    .build();
        }
    }

    /**
     * 해지상담 등록 및 알림 스케줄링
     */
    public CustomerManageCommandDTO.CommandResponse registerTermination(CustomerManageCommandDTO.TerminationRequest request, Integer counselorId) {
        try {
            // beneficiary_history에 해지일 등록
            BeneficiaryHistory history = beneficiaryHistoryRepository
                    .findByBeneficiaryId(request.getBeneficiaryId())
                    .orElse(null);

            if (history != null) {
                history.setTerminateDate(request.getPlannedTerminationDate().atStartOfDay());
                beneficiaryHistoryRepository.save(history);
            } else {
                // history가 없으면 새로 생성
                history = new BeneficiaryHistory();
                history.setBeneficiaryId(request.getBeneficiaryId());
                history.setJoinDate(LocalDateTime.now()); // 임시값
                history.setTerminateDate(request.getPlannedTerminationDate().atStartOfDay());
                beneficiaryHistoryRepository.save(history);
            }

            // 해지 상담 이력 등록
            CounselHistory counselHistory = CounselHistory.builder()
                    .consultDate(LocalDateTime.now())
                    .summary("해지 상담 등록")
                    .detail(request.getTerminationReason() != null ? request.getTerminationReason() : "고객 요청에 의한 해지")
                    .guardianSt(0)
                    .followUpNecessary("Y")
                    .churn("Y")
                    .churnReason(request.getTerminationReason())
                    .counselCategoryId(5) // 해지 카테고리
                    .beneficiaryId(request.getBeneficiaryId())
                    .counselorId(counselorId)
                    .build();
            counselHistoryRepository.save(counselHistory);

            CustomerManageDTO.CustomerManageDetail detail = customerManageMapper.selectCustomerManageDetail(request.getBeneficiaryId());

            // 현재 날짜와 해지 예정일 사이의 일수 계산
            long daysUntilTermination = ChronoUnit.DAYS.between(LocalDate.now(), request.getPlannedTerminationDate());

            // 15일 전 알림 (상담사에게)
            if (daysUntilTermination >= 15) {
                scheduleTerminationNotification(
                        request.getBeneficiaryId(),
                        detail.getName(),
                        counselorId,
                        15,
                        "상담사"
                );
            }

            // 3일 전 알림 (담당 요양보호사에게)
            Integer careWorkerId = customerManageMapper.selectCareWorkerId(request.getBeneficiaryId());
            if (careWorkerId != null && daysUntilTermination >= 3) {
                scheduleTerminationNotification(
                        request.getBeneficiaryId(),
                        detail.getName(),
                        careWorkerId,
                        3,
                        "요양보호사"
                );
            }

            // 즉시 알림 (해지 등록 알림)
            String title = "[해지상담] 해지 예정 등록";
            String content = String.format(
                    "수급자 '%s'님의 해지 예정일이 %s로 등록되었습니다.",
                    detail.getName(),
                    request.getPlannedTerminationDate().toString()
            );

            notificationCommandService.sendCustom(
                    Long.valueOf(counselorId),
                    title,
                    content,
                    "해지상담",
                    SEVERITY_URGENT
            );

            log.info("해지 등록 완료 - beneficiaryId: {}, terminationDate: {}",
                    request.getBeneficiaryId(), request.getPlannedTerminationDate());

            return CustomerManageCommandDTO.CommandResponse.builder()
                    .success(true)
                    .message("해지가 등록되었습니다. 해지 예정일: " + request.getPlannedTerminationDate())
                    .data(request.getPlannedTerminationDate())
                    .build();

        } catch (Exception e) {
            log.error("해지 등록 실패", e);
            return CustomerManageCommandDTO.CommandResponse.builder()
                    .success(false)
                    .message("해지 등록에 실패했습니다: " + e.getMessage())
                    .build();
        }
    }

    /**
     * 해지 예정 알림 스케줄링 (실제로는 스케줄러에서 처리해야 하지만, 여기서는 즉시 발송)
     */
    private void scheduleTerminationNotification(Long beneficiaryId, String beneficiaryName,
                                                 Integer receiverId, int daysBefore, String receiverType) {
        String title = String.format("[해지예정] %d일 전 알림", daysBefore);
        String content = String.format(
                "수급자 '%s'님의 해지 예정일이 %d일 남았습니다. 후속 상담을 진행해 주세요.",
                beneficiaryName,
                daysBefore
        );

        log.info("해지 {}일 전 알림 예약 - beneficiaryId: {}, receiverId: {}, receiverType: {}",
                daysBefore, beneficiaryId, receiverId, receiverType);

        // 실제 구현 시에는 스케줄러를 통해 예약 발송해야 함
        // 여기서는 로그만 남기고, 실제 알림은 NotificationScheduler에서 처리
    }

    /**
     * 렌탈상담 - 요양보호사에게 알림 발송
     */
    public CustomerManageCommandDTO.CommandResponse sendRentalNotification(Long beneficiaryId) {
        try {
            CustomerManageDTO.CustomerManageDetail detail = customerManageMapper.selectCustomerManageDetail(beneficiaryId);
            Integer careWorkerId = customerManageMapper.selectCareWorkerId(beneficiaryId);

            if (careWorkerId == null) {
                return CustomerManageCommandDTO.CommandResponse.builder()
                        .success(false)
                        .message("담당 요양보호사가 지정되지 않았습니다.")
                        .build();
            }

            String title = "[렌탈상담] 렌탈 상담 등록";
            String content = String.format(
                    "수급자 '%s'님의 렌탈 상담이 등록되었습니다. 방문 시 렌탈 용품에 대해 안내해 주세요.",
                    detail.getName()
            );

            notificationCommandService.sendCustom(
                    Long.valueOf(careWorkerId),
                    title,
                    content,
                    "렌탈상담",
                    SEVERITY_NORMAL
            );

            log.info("렌탈 알림 발송 완료 - beneficiaryId: {}, careWorkerId: {}", beneficiaryId, careWorkerId);

            return CustomerManageCommandDTO.CommandResponse.builder()
                    .success(true)
                    .message("렌탈 상담 알림이 요양보호사에게 발송되었습니다.")
                    .build();

        } catch (Exception e) {
            log.error("렌탈 알림 발송 실패", e);
            return CustomerManageCommandDTO.CommandResponse.builder()
                    .success(false)
                    .message("알림 발송에 실패했습니다: " + e.getMessage())
                    .build();
        }
    }
}
