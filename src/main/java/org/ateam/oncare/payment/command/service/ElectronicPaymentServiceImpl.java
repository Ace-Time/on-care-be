package org.ateam.oncare.payment.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.payment.command.dto.ElectronicPaymentCreate;
import org.ateam.oncare.payment.command.entity.ElectronicPayment;
import org.ateam.oncare.payment.command.entity.ElectronicPaymentProcess;
import org.ateam.oncare.payment.command.repository.ElectronicPaymentProcessRepository;
import org.ateam.oncare.payment.command.repository.ElectronicPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ElectronicPaymentServiceImpl implements ElectronicPaymentService {
    private final ElectronicPaymentRepository paymentRepository;
    private final ElectronicPaymentProcessRepository processRepository;

    @Transactional
    @Override
    public Long createPayment(ElectronicPaymentCreate request) {
        // 1. 결재 문서(Master) 저장
        ElectronicPayment payment = ElectronicPayment.builder()
                .employeeId(Long.valueOf(request.getEmployeeId()))
                .electronicPaymentCategoryId(Long.valueOf(request.getCategoryId()))
                .title(request.getTitle())
                .content(request.getContent())
                .priority(request.getPriority())
                .status(0) // 0: 대기
                .amount(request.getAmount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        paymentRepository.save(payment);

        // 2. 결재선(Detail) 저장
        int step = 1;
        for (Integer approverId : request.getApproverIds()) {
            ElectronicPaymentProcess process = ElectronicPaymentProcess.builder()
                    .electronicPaymentId(payment.getId())
                    .employeeId(Long.valueOf(approverId))
                    .stepOrder(step++)
                    .status(0) // 0: 대기
                    .build();

            processRepository.save(process);
        }

        // 생성된 문서 ID 반환
        return Long.valueOf(payment.getId());
    }
}
