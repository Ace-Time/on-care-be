package org.ateam.oncare.payment.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.payment.command.dto.ElectronicPaymentCreate;
import org.ateam.oncare.payment.command.entity.ElectronicPayment;
import org.ateam.oncare.payment.command.entity.ElectronicPaymentProcess;
import org.ateam.oncare.payment.command.repository.ElectronicPaymentProcessRepository;
import org.ateam.oncare.payment.command.repository.ElectronicPaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ElectronicPaymentServiceImpl implements ElectronicPaymentService {
    private final ElectronicPaymentRepository paymentRepository;
    private final ElectronicPaymentProcessRepository processRepository;

    @Transactional
    @Override
    public Long createPayment(ElectronicPaymentCreate request) {
        // 1. 문서 번호 생성 로직 (예: 날짜 + UUID 일부)
        String docNumber = generateDocumentNumber();

        // 2. 결재 문서(Master) 저장
        ElectronicPayment payment = ElectronicPayment.builder()
                .number(docNumber)
                .employeeId(request.getEmployeeId())
                .categoryId(request.getCategoryId())
                .build();

        paymentRepository.save(payment);

        // 3. 결재선(Detail) 저장
        // 요청받은 결재자 목록(List)을 순회하며 Process 테이블에 저장
        // 로직 가정: drafter_id는 문서를 작성한 사람(employeeId)으로 설정하고, approver_id는 지정된 결재자로 설정
        for (Integer approverId : request.getApproverIds()) {
            ElectronicPaymentProcess process = ElectronicPaymentProcess.builder()
                    .drafterId(request.getEmployeeId()) // 기안자
                    .approverId(approverId)             // 승인자
                    .electronicPayment(payment)         // 연관관계 매핑
                    .build();

            processRepository.save(process);
        }

        // 생성된 문서 ID 반환
        return Long.valueOf(payment.getId());
    }

    // 문서 번호 생성 헬퍼 메서드
    private String generateDocumentNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "DOC-" + date + "-" + uuid;

    }
}
