package org.ateam.oncare.counsel.command.service;

import org.ateam.oncare.beneficiary.command.entity.BeneficiarySignificant;
import org.ateam.oncare.beneficiary.command.repository.BeneficiarySignificantRepository;
import org.ateam.oncare.beneficiary.command.service.BeneficiaryRegistService;
import org.ateam.oncare.counsel.command.dto.RegistNewBeneficiary;
import org.ateam.oncare.counsel.command.dto.RegistNewBeneficiaryResponse;
import org.ateam.oncare.employee.command.repository.BeneficiaryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CustomerFacadeServiceTest {

    @InjectMocks
    private CustomerFacadeService customerFacadeService;

    @Mock private BeneficiaryRegistService beneficiaryRegistService;
    @Mock private BeneficiaryRepository beneficiaryRepository;
    @Mock private BeneficiarySignificantRepository beneficiarySignificantRepository;

    /**
     * 1. 신규 수급자 등록 성공 테스트
     * 시나리오: 중복되지 않은 고객이 들어왔을 때, 모든 등록 로직(Service 호출)이 순차적으로 실행되고 ID가 반환된다.
     */
    @Test
    @DisplayName("신규 수급자 등록 성공 - 모든 등록 프로세스 호출 검증")
    void registNewBeneficiary_Success() {
        // given
        RegistNewBeneficiary request = new RegistNewBeneficiary();
        request.setPotentialCustomerId(100L); // 잠재고객 ID 설정

        BigInteger mockBeneficiaryId = BigInteger.valueOf(500L);
        int mockCareLevelId = 10;

        // Mocking: 중복 체크 통과(false) -> 수급자 ID 반환 -> 요양등급 ID 반환
        given(beneficiaryRepository.existsByPotentialCustomerId(100L)).willReturn(false);
        given(beneficiaryRegistService.registBeneficiaryAndReturnId(request)).willReturn(mockBeneficiaryId);
        given(beneficiaryRegistService.registCareLevelAndReturnId(mockBeneficiaryId, request)).willReturn(mockCareLevelId);

        // when
        ResponseEntity<RegistNewBeneficiaryResponse> response = customerFacadeService.registNewBeneficiary(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isTrue();
        assertThat(response.getBody().getBeneficiaryId()).isEqualTo(mockBeneficiaryId);

        // 핵심 로직들이 모두 호출되었는지 검증 (순서가 중요한 경우 InOrder 사용 가능하나 여기선 호출 여부만 체크)
        verify(beneficiaryRegistService).registCount(eq(mockCareLevelId), eq(request));
        verify(beneficiaryRegistService).registExpiration(mockBeneficiaryId);
        verify(beneficiaryRegistService).registRiskOfMember(mockBeneficiaryId, request);
        verify(beneficiaryRegistService).registBeneficiarySchedule(mockBeneficiaryId, request);
        verify(beneficiaryRegistService).registHistory(mockBeneficiaryId, request);
    }

    /**
     * 2. 신규 수급자 등록 실패 테스트 (중복)
     * 시나리오: 이미 등록된 PotentialCustomerId로 요청 시 400 Bad Request와 실패 메시지를 반환한다.
     */
    @Test
    @DisplayName("신규 수급자 등록 실패 - 이미 등록된 수급자(중복)")
    void registNewBeneficiary_Fail_Duplicate() {
        // given
        RegistNewBeneficiary request = new RegistNewBeneficiary();
        request.setPotentialCustomerId(999L);

        // Mocking: 이미 존재함(true)
        given(beneficiaryRepository.existsByPotentialCustomerId(999L)).willReturn(true);

        // when
        ResponseEntity<RegistNewBeneficiaryResponse> response = customerFacadeService.registNewBeneficiary(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).contains("이미 수급자로 등록되어 있습니다");

        // 중요: 실패했으므로 등록 서비스(registBeneficiaryAndReturnId)는 절대 호출되면 안 됨
        verify(beneficiaryRegistService, times(0)).registBeneficiaryAndReturnId(any());
    }

    /**
     * 3. 수급자 특이사항 추가 테스트 (단순 저장 로직)
     * 시나리오: 파라미터로 받은 ID들을 이용해 엔티티를 생성하고 Repository.save()를 호출한다.
     */
    @Test
    @DisplayName("수급자 특이사항 추가 - 엔티티 생성 및 저장 검증")
    void addBeneficiarySignificant_Success() {
        // given
        BigInteger beneficiaryId = BigInteger.valueOf(123L);
        Integer significantId = 5;

        // Captor: Repository.save() 메서드에 전달된 인자를 가로채서 검증하기 위함
        ArgumentCaptor<BeneficiarySignificant> captor = ArgumentCaptor.forClass(BeneficiarySignificant.class);

        // when
        customerFacadeService.addBeneficiarySignificant(beneficiaryId, significantId);

        // then
        verify(beneficiarySignificantRepository).save(captor.capture()); // save 호출 시 인자 캡처

        BeneficiarySignificant savedEntity = captor.getValue();
        assertThat(savedEntity.getBeneficiaryId()).isEqualTo(123L);
        assertThat(savedEntity.getSignificantId()).isEqualTo(5L);
    }
}