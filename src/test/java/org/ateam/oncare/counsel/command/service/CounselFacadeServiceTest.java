package org.ateam.oncare.counsel.command.service;

import org.ateam.oncare.counsel.command.dto.*;
import org.ateam.oncare.counsel.command.entity.CounselHistory;
import org.ateam.oncare.counsel.command.repository.PotentialCustomerRepository;
import org.ateam.oncare.employee.command.repository.BeneficiaryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CounselFacadeServiceTest {

    @InjectMocks
    private CounselFacadeService counselFacadeService;

    @Mock private CounselRegistrationService counselRegistrationService;
    @Mock private PotentialStageService potentialStageService;
    @Mock private PotentialCustomerService potentialCustomerService;
    @Mock private BeneficiaryRepository beneficiaryRepository;
    @Mock private PotentialCustomerRepository potentialCustomerRepository;

    @Test
    @DisplayName("신규 가입상담 등록 성공 테스트")
    void registNewSubscription_Success() {
        // given
        Subscription request = new Subscription();
        request.setName("홍길동");
        request.setPhone("010-1234-5678");

        BigInteger mockPotentialId = BigInteger.ONE;

        // [수정] 서비스 로직에서 꺼내 쓰는 모든 필드에 값을 채워넣음
        CounselHistory mockHistory = CounselHistory.builder()
                .id(100L)
                .potentialId(1L)
                .counselCategoryId(1)       // 오류 발생했던 부분 1
                .reservationChannelId(1)    // 오류 발생했던 부분 2
                .counselorId(10)           // (잠재적 오류 방지) 상담원 ID
                .consultDate(LocalDateTime.now()) // (잠재적 오류 방지) 상담일
                .detail("상담 내용 상세")
                .summary("상담 요약")
                .churn("N")
                .followUpNecessary("N")
                .build();

        given(potentialCustomerService.registPotentialCustomer(any(), any()))
                .willReturn(mockPotentialId);
        given(counselRegistrationService.registSubscription(any()))
                .willReturn(mockHistory);

        // when
        ResponseEntity<NewSubscriptionResponse> response = counselFacadeService.registNewSubscription(request);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCounselHistoryId()).isEqualTo(BigInteger.valueOf(100L));
        assertThat(response.getBody().getReservationChannelId()).isEqualTo(1); // 검증 추가

        verify(potentialStageService).registPotentialStage(request, mockPotentialId);
    }

    @Test
    @DisplayName("기존 잠재고객 카테고리 상담 등록 시 스테이지 데이터 포함 여부 테스트")
    void registSubscription_PotentialCategory() {
        // given
        Subscription request = new Subscription();
        BigInteger customerId = BigInteger.valueOf(50L);
        String categoryName = "잠재고객";

        // [수정] 여기도 값을 꽉 채운 객체로 생성
        CounselHistory mockHistory = CounselHistory.builder()
                .id(200L)
                .potentialId(50L)
                .counselCategoryId(2)
                .reservationChannelId(3)
                .counselorId(10)
                .consultDate(LocalDateTime.now())
                .churn("N")
                .build();

        Map<Integer, StageData> mockStageData = Map.of(1, new StageData());

        given(potentialStageService.findStageDataByPotentialId(50L))
                .willReturn(mockStageData);
        given(counselRegistrationService.registSubscription(any()))
                .willReturn(mockHistory);

        // when
        ResponseEntity<SubscriptionResponse> response =
                counselFacadeService.registSubscription(request, customerId, "POTENTIAL", categoryName);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStageData()).isEqualTo(mockStageData);
    }

    @Test
    @DisplayName("잠재고객 등록 실패 - 이미 존재하는 기존고객 전화번호")
    void registPotentialCustomer_Fail_DuplicateBeneficiary() {
        // given
        RegistPotentialCustomer request = new RegistPotentialCustomer();
        request.setPhone("010-9999-8888");
        request.setName("이미존재");

        String normalizedPhone = "01099998888";

        given(beneficiaryRepository.existsByPhoneNormalized(normalizedPhone))
                .willReturn(true);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            counselFacadeService.registPotentialCustomer(request);
        });

        assertThat(exception.getMessage()).isEqualTo("이미 등록된 기존고객입니다.");
    }
}