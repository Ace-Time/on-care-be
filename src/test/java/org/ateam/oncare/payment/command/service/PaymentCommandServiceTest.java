package org.ateam.oncare.payment.command.service;

import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.payment.command.dto.PaymentActionRequest;
import org.ateam.oncare.payment.command.dto.PaymentCreateRequest;
import org.ateam.oncare.payment.command.entity.ElectronicPayment;
import org.ateam.oncare.payment.command.entity.ElectronicPaymentProcess;
import org.ateam.oncare.payment.command.repository.ElectronicPaymentProcessRepository;
import org.ateam.oncare.payment.command.repository.ElectronicPaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCommandServiceTest {

    @Mock
    private ElectronicPaymentRepository paymentRepository;
    @Mock
    private ElectronicPaymentProcessRepository processRepository;
    @Mock
    private NotificationCommandService notificationCommandService;

    @InjectMocks
    private PaymentCommandServiceImpl paymentService;

    @Test
    @DisplayName("결재 문서 기안(생성) 테스트")
    void testCreatePayment() {
        // Given
        Long drafterId = 200L;
        PaymentCreateRequest request = new PaymentCreateRequest();
        request.setCategoryId(1L);
        request.setTitle("비품 구매 요청");
        request.setContent("A4 용지 등");
        request.setAmount(BigDecimal.valueOf(50000));
        request.setApproverIds(List.of(201L, 202L)); // 결재자 2명

        ElectronicPayment savedPayment = new ElectronicPayment();
        savedPayment.setId(1000);
        when(paymentRepository.save(any(ElectronicPayment.class))).thenReturn(savedPayment);

        // When
        paymentService.createPayment(drafterId, request);

        // Then
        verify(paymentRepository, times(1)).save(any(ElectronicPayment.class));
        verify(processRepository, times(2)).save(any(ElectronicPaymentProcess.class));
        verify(notificationCommandService, times(1)).send(anyList(), eq(6L));
    }

    @Test
    @DisplayName("결재 승인 처리 테스트")
    void testProcessPayment_Approve() {
        // Given
        Long approverId = 201L;
        Long paymentId = 1000L;

        PaymentActionRequest actionRequest = new PaymentActionRequest();
        actionRequest.setAction("APPROVE");
        actionRequest.setComment("승인합니다.");

        ElectronicPayment payment = new ElectronicPayment();
        payment.setId(1000);
        payment.setStatus(0); // 대기

        ElectronicPaymentProcess myProcess = new ElectronicPaymentProcess();
        myProcess.setId(1);
        myProcess.setEmployeeId(approverId);
        myProcess.setStatus(0);
        myProcess.setStepOrder(1);

        // 다음 결재자가 있다고 가정
        ElectronicPaymentProcess nextProcess = new ElectronicPaymentProcess();
        nextProcess.setId(2);
        nextProcess.setEmployeeId(202L);
        nextProcess.setStatus(0);
        nextProcess.setStepOrder(2);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(processRepository.findByElectronicPaymentIdAndEmployeeIdAndStatus(1000, approverId, 0))
                .thenReturn(Optional.of(myProcess));
        when(processRepository.findByElectronicPaymentIdOrderByStepOrderAsc(1000))
                .thenReturn(List.of(myProcess, nextProcess));

        // When
        paymentService.processPayment(approverId, paymentId, actionRequest);

        // Then
        // 내 결재 상태가 1(승인)로 변경되었는지 확인
        assert (myProcess.getStatus() == 1);
        // 다음 결재자가 있으므로 문서는 아직 승인(1)되지 않아야 함 (여기서는 로직 상 setStatus(1)을 호출 안함)
        // 만약 마지막 결재자라면 문서 상태도 1로 변함.
    }

    @Test
    @DisplayName("결재 반려 처리 및 이후 단계 반려 처리 테스트")
    void testProcessPayment_Reject() {
        // Given
        Long approverId = 201L;
        Long paymentId = 1000L;

        PaymentActionRequest actionRequest = new PaymentActionRequest();
        actionRequest.setAction("REJECT");
        actionRequest.setComment("반려입니다.");

        ElectronicPayment payment = new ElectronicPayment();
        payment.setId(1000);
        payment.setStatus(0);
        // 필요시 employeeId 세팅 (알림 발송용)
        payment.setEmployeeId(999L);

        ElectronicPaymentProcess myProcess = new ElectronicPaymentProcess();
        myProcess.setId(1);
        myProcess.setEmployeeId(approverId);
        myProcess.setStatus(0);
        myProcess.setStepOrder(1);

        ElectronicPaymentProcess nextProcess = new ElectronicPaymentProcess();
        nextProcess.setId(2);
        nextProcess.setStatus(0);
        nextProcess.setStepOrder(2);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(processRepository.findByElectronicPaymentIdAndEmployeeIdAndStatus(1000, approverId, 0))
                .thenReturn(Optional.of(myProcess));
        when(processRepository.findByElectronicPaymentIdOrderByStepOrderAsc(1000))
                .thenReturn(List.of(myProcess, nextProcess));

        // When
        paymentService.processPayment(approverId, paymentId, actionRequest);

        // Then
        // 내 결재 상태 2(반려)
        assert (myProcess.getStatus() == 2);
        // 문서 상태 2(반려)
        assert (payment.getStatus() == 2);
        // 다음 결재 단계도 2(반려)
        assert (nextProcess.getStatus() == 2);
    }

    @Test
    @DisplayName("최종 결재자 승인 시 문서 전체 승인 테스트")
    void testProcessPayment_FinalApproval() {
        // Given
        Long approverId = 202L;
        Long paymentId = 1000L;
        PaymentActionRequest actionRequest = new PaymentActionRequest();
        actionRequest.setAction("APPROVE");

        ElectronicPayment payment = new ElectronicPayment();
        payment.setId(1000);
        payment.setStatus(0);
        payment.setEmployeeId(999L);

        ElectronicPaymentProcess prevProcess = new ElectronicPaymentProcess();
        prevProcess.setId(1);
        prevProcess.setStatus(1); // 이미 승인됨
        prevProcess.setStepOrder(1);

        ElectronicPaymentProcess myProcess = new ElectronicPaymentProcess();
        myProcess.setId(2);
        myProcess.setEmployeeId(approverId);
        myProcess.setStatus(0);
        myProcess.setStepOrder(2);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(processRepository.findByElectronicPaymentIdAndEmployeeIdAndStatus(1000, approverId, 0))
                .thenReturn(Optional.of(myProcess));
        when(processRepository.findByElectronicPaymentIdOrderByStepOrderAsc(1000))
                .thenReturn(List.of(prevProcess, myProcess)); // 내가 마지막

        // When
        paymentService.processPayment(approverId, paymentId, actionRequest);

        // Then
        // 문서 상태가 1(승인)로 변경되어야 함
        assert (payment.getStatus() == 1);
    }

    @Test
    @DisplayName("이전 결재자가 승인하지 않은 상태에서 결재 시도 시 예외 발생 테스트")
    void testProcessPayment_InvalidOrder() {
        // Given
        Long approverId = 202L;
        Long paymentId = 1000L;
        PaymentActionRequest actionRequest = new PaymentActionRequest();
        actionRequest.setAction("APPROVE");

        ElectronicPayment payment = new ElectronicPayment();
        payment.setId(1000);

        ElectronicPaymentProcess prevProcess = new ElectronicPaymentProcess();
        prevProcess.setId(1);
        prevProcess.setStatus(0); // 아직 승인 안됨 (대기)
        prevProcess.setStepOrder(1);

        ElectronicPaymentProcess myProcess = new ElectronicPaymentProcess();
        myProcess.setId(2);
        myProcess.setEmployeeId(approverId);
        myProcess.setStatus(0);
        myProcess.setStepOrder(2);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(processRepository.findByElectronicPaymentIdAndEmployeeIdAndStatus(1000, approverId, 0))
                .thenReturn(Optional.of(myProcess));
        // 순서 체크 로직에서 전체 리스트를 조회함
        when(processRepository.findByElectronicPaymentIdOrderByStepOrderAsc(1000))
                .thenReturn(List.of(prevProcess, myProcess));

        // When & Then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> paymentService.processPayment(approverId, paymentId, actionRequest));
    }
}
