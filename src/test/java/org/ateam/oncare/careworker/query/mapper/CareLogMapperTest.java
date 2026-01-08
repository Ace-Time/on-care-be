package org.ateam.oncare.careworker.query.mapper;

import org.ateam.oncare.careworker.query.dto.CareLogDetailDto;
import org.ateam.oncare.careworker.query.dto.CareLogListDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CareLogMapperTest {

    @Mock
    private CareLogMapper careLogMapper;

    private CareLogListDto mockCareLogListDto;
    private CareLogDetailDto mockCareLogDetailDto;

    @BeforeEach
    void setUp() {
        // Mock 데이터 준비
        mockCareLogListDto = new CareLogListDto();
        mockCareLogListDto.setLogId(1L);
        mockCareLogListDto.setBeneficiaryId(1L);
        mockCareLogListDto.setBeneficiaryName("김수급");
        mockCareLogListDto.setServiceDate(LocalDate.of(2024, 1, 15));
        mockCareLogListDto.setCareWorkerName("이요양");

        mockCareLogDetailDto = new CareLogDetailDto();
        mockCareLogDetailDto.setLogId(1L);
        mockCareLogDetailDto.setBeneficiaryId(1L);
        mockCareLogDetailDto.setBeneficiaryName("김수급");
        mockCareLogDetailDto.setServiceDate(LocalDate.of(2024, 1, 15));
        mockCareLogDetailDto.setStartTime(LocalTime.of(9, 0));
        mockCareLogDetailDto.setEndTime(LocalTime.of(17, 0));
        mockCareLogDetailDto.setCareWorkerName("이요양");
        mockCareLogDetailDto.setSpecialNote("특이사항 없음");
    }

    @Test
    @DisplayName("요양일지 목록 조회(요양보호사별): 요양보호사의 요양일지 목록이 조회되어야 한다")
    void selectCareLogList_shouldReturnListByEmployee() {
        // Given
        Long employeeId = 1L;
        when(careLogMapper.selectCareLogList(employeeId)).thenReturn(List.of(mockCareLogListDto));

        // When
        List<CareLogListDto> careLogs = careLogMapper.selectCareLogList(employeeId);

        // Then
        assertThat(careLogs).isNotNull();
        assertThat(careLogs).hasSize(1);

        System.out.println("=== 요양일지 목록 조회 (요양보호사별) ===");
        System.out.println("요양보호사 ID: " + employeeId);
        System.out.println("요양일지 개수: " + careLogs.size());

        CareLogListDto firstLog = careLogs.get(0);
        System.out.println("첫 번째 요양일지:");
        System.out.println("  - ID: " + firstLog.getLogId());
        System.out.println("  - 수급자: " + firstLog.getBeneficiaryName());
        System.out.println("  - 서비스 날짜: " + firstLog.getServiceDate());
        System.out.println("  - 작성자: " + firstLog.getCareWorkerName());

        // 작성자 이름이 조회되는지 확인
        assertThat(firstLog.getCareWorkerName()).isNotNull();
        assertThat(firstLog.getLogId()).isEqualTo(1L);
        assertThat(firstLog.getBeneficiaryName()).isEqualTo("김수급");
        System.out.println("====================================");
    }

    @Test
    @DisplayName("요양일지 목록 조회(수급자별): 수급자의 요양일지 목록이 조회되어야 한다")
    void selectCareLogListByBeneficiary_shouldReturnListByBeneficiary() {
        // Given
        Long beneficiaryId = 1L;
        when(careLogMapper.selectCareLogListByBeneficiary(beneficiaryId)).thenReturn(List.of(mockCareLogListDto));

        // When
        List<CareLogListDto> careLogs = careLogMapper.selectCareLogListByBeneficiary(beneficiaryId);

        // Then
        assertThat(careLogs).isNotNull();
        assertThat(careLogs).hasSize(1);

        System.out.println("=== 요양일지 목록 조회 (수급자별) ===");
        System.out.println("수급자 ID: " + beneficiaryId);
        System.out.println("요양일지 개수: " + careLogs.size());

        System.out.println("요양일지 목록:");
        careLogs.forEach(log -> System.out.println("  - " + log.getServiceDate()
                + " / " + log.getCareWorkerName()));

        // 모든 요양일지가 동일한 수급자의 것인지 확인
        boolean allSameBeneficiary = careLogs.stream()
                .allMatch(log -> log.getBeneficiaryId().equals(beneficiaryId));
        assertThat(allSameBeneficiary).isTrue();
        System.out.println("=================================");
    }

    @Test
    @DisplayName("요양일지 상세 조회: 요양일지의 상세 정보가 조회되어야 한다")
    void selectCareLogDetail_shouldReturnDetail() {
        // Given
        Long logId = 1L;
        when(careLogMapper.selectCareLogDetail(anyLong())).thenReturn(mockCareLogDetailDto);

        // When
        CareLogDetailDto careLogDetail = careLogMapper.selectCareLogDetail(logId);

        // Then
        System.out.println("=== 요양일지 상세 조회 ===");

        assertThat(careLogDetail).isNotNull();
        System.out.println("요양일지 ID: " + careLogDetail.getLogId());
        System.out.println("수급자: " + careLogDetail.getBeneficiaryName());
        System.out.println("서비스 날짜: " + careLogDetail.getServiceDate());
        System.out.println("작성자: " + careLogDetail.getCareWorkerName());
        System.out.println("특이사항: " + careLogDetail.getSpecialNote());

        assertThat(careLogDetail.getLogId()).isEqualTo(logId);
        assertThat(careLogDetail.getBeneficiaryName()).isNotNull();
        assertThat(careLogDetail.getServiceDate()).isNotNull();
        assertThat(careLogDetail.getCareWorkerName()).isNotNull();
        System.out.println("========================");
    }
}
