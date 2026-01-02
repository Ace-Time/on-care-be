package org.ateam.oncare.payment.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.payment.command.dto.PaymentActionRequest;
import org.ateam.oncare.payment.command.dto.PaymentCreateRequest;
import org.ateam.oncare.payment.command.entity.ElectronicPayment;
import org.ateam.oncare.payment.command.entity.ElectronicPaymentProcess;
import org.ateam.oncare.payment.command.repository.ElectronicPaymentProcessRepository;
import org.ateam.oncare.payment.command.repository.ElectronicPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final ElectronicPaymentRepository paymentRepository;
    private final ElectronicPaymentProcessRepository processRepository;
    private final org.ateam.oncare.alarm.command.service.NotificationCommandService notificationCommandService;

    @Override
    public void createPayment(Long drafterId, PaymentCreateRequest request) {
        ElectronicPayment payment = new ElectronicPayment();
        payment.setEmployeeId(drafterId);
        payment.setElectronicPaymentCategoryId(request.getCategoryId());
        payment.setTitle(request.getTitle());
        payment.setContent(request.getContent());
        payment.setPriority(request.getPriority());
        payment.setStatus(0); // 0: 대기 (최초 생성 시)
        payment.setAmount(request.getAmount());
        payment.setStartDate(request.getStartDate());
        payment.setEndDate(request.getEndDate());

        ElectronicPayment savedPayment = paymentRepository.save(payment);

        // 결재선(Process) 생성
        List<Long> approverIds = request.getApproverIds();
        if (approverIds != null && !approverIds.isEmpty()) {
            for (int i = 0; i < approverIds.size(); i++) {
                ElectronicPaymentProcess process = new ElectronicPaymentProcess();
                process.setElectronicPaymentId(savedPayment.getId());
                process.setEmployeeId(approverIds.get(i));
                process.setStepOrder(i + 1);
                process.setStatus(0); // 0: 대기
                processRepository.save(process);
            }

            // 결재자들에게 알림 전송 (Template ID: 7)
            try {
                notificationCommandService.send(approverIds, 7L);
            } catch (Exception e) {
                // 알림 전송 실패 로그
                System.err.println("알림 전송 실패: " + e.getMessage());
            }
        }
    }

    @Override
    public void processPayment(Long approverId, Long paymentId, PaymentActionRequest request) {
        // 해당 문서 존재 여부 확인
        ElectronicPayment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 결재 문서입니다."));

        // 내 결재 순서 조회 (현재 문서에서 내가 결재해야 할 '대기(0)' 상태인 단계 찾기)
        ElectronicPaymentProcess myProcess = processRepository.findByElectronicPaymentIdAndEmployeeIdAndStatus(
                payment.getId(), approverId, 0)
                .orElseThrow(() -> new IllegalArgumentException("현재 처리할 수 있는 결재 내역이 없습니다. (이미 처리되었거나 본인 차례가 아님)"));

        // 내가 현재 결재해야 할 순서인지 확인 (내 앞 순서가 모두 승인되었는지 확인)

        List<ElectronicPaymentProcess> allProcesses = processRepository
                .findByElectronicPaymentIdOrderByStepOrderAsc(payment.getId());

        // 가장 먼저 대기중인 프로세스 찾기
        ElectronicPaymentProcess currentStep = allProcesses.stream()
                .filter(p -> p.getStatus() == 0) // 대기중인 것 중
                .findFirst() // 첫 번째(순서 가장 빠른 것)
                .orElseThrow(() -> new IllegalArgumentException("대기 중인 결재 단계가 없습니다."));

        if (!currentStep.getId().equals(myProcess.getId())) {
            throw new IllegalArgumentException("이전 결재자가 아직 승인하지 않았습니다.");
        }

        // 승인/반려 처리
        LocalDateTime now = LocalDateTime.now();
        myProcess.setProcessedAt(now);
        myProcess.setComment(request.getComment());

        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
            // 승인 처리
            myProcess.setStatus(1); // 1: 승인

            // 다음 결재자가 있는지 확인
            boolean hasNext = allProcesses.stream()
                    .anyMatch(p -> p.getStepOrder() > myProcess.getStepOrder());

            if (!hasNext) {
                // 더 이상 결재자가 없으면 문서 전체 승인 처리
                payment.setStatus(1); // 1: 승인

                // 승인 완료 알림 (기안자에게) - Template ID: 8
                try {
                    notificationCommandService.send(payment.getEmployeeId(), 8L);
                } catch (Exception e) {
                    System.err.println("승인 알림 전송 실패: " + e.getMessage());
                }
            }
        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {
            // 반려 처리
            myProcess.setStatus(2); // 2: 반려
            payment.setStatus(2); // 문서 전체 반려

            // 반려 알림 (기안자에게) - Template ID: 9
            try {
                notificationCommandService.send(payment.getEmployeeId(), 9L);
            } catch (Exception e) {
                System.err.println("반려 알림 전송 실패: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("유효하지 않은 결재 동작입니다: " + request.getAction());
        }
    }
}
