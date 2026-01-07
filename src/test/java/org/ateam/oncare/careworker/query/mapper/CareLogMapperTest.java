package org.ateam.oncare.careworker.query.mapper;

import org.ateam.oncare.careworker.query.dto.CareLogDetailDto;
import org.ateam.oncare.careworker.query.dto.CareLogListDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class CareLogMapperTest {

    @Autowired
    private CareLogMapper careLogMapper;

    @Test
    @DisplayName("요양일지 목록 조회(요양보호사별): 요양보호사의 요양일지 목록이 조회되어야 한다")
    void selectCareLogList_shouldReturnListByEmployee() {
        // Given
        Long employeeId = 1L; // dev DB에 존재하는 요양보호사(employee) ID

        // When
        List<CareLogListDto> careLogs = careLogMapper.selectCareLogList(employeeId);

        // Then
        assertThat(careLogs).isNotNull();

        System.out.println("=== 요양일지 목록 조회 (요양보호사별) ===");
        System.out.println("요양보호사 ID: " + employeeId);
        System.out.println("요양일지 개수: " + careLogs.size());

        if (!careLogs.isEmpty()) {
            CareLogListDto firstLog = careLogs.get(0);
            System.out.println("첫 번째 요양일지:");
            System.out.println("  - ID: " + firstLog.getLogId());
            System.out.println("  - 수급자: " + firstLog.getBeneficiaryName());
            System.out.println("  - 서비스 날짜: " + firstLog.getServiceDate());
            System.out.println("  - 작성자: " + firstLog.getCareWorkerName());

            // 작성자 이름이 조회되는지 확인
            assertThat(firstLog.getCareWorkerName()).isNotNull();
        }
        System.out.println("====================================");
    }

    @Test
    @DisplayName("요양일지 목록 조회(수급자별): 수급자의 요양일지 목록이 조회되어야 한다")
    void selectCareLogListByBeneficiary_shouldReturnListByBeneficiary() {
        // Given
        Long beneficiaryId = 1L; // dev DB에 존재하는 수급자 ID

        // When
        List<CareLogListDto> careLogs = careLogMapper.selectCareLogListByBeneficiary(beneficiaryId);

        // Then
        assertThat(careLogs).isNotNull();

        System.out.println("=== 요양일지 목록 조회 (수급자별) ===");
        System.out.println("수급자 ID: " + beneficiaryId);
        System.out.println("요양일지 개수: " + careLogs.size());

        if (!careLogs.isEmpty()) {
            System.out.println("요양일지 목록:");
            careLogs.forEach(log -> {
                System.out.println("  - " + log.getServiceDate()
                        + " / " + log.getCareWorkerName());
            });

            // 모든 요양일지가 동일한 수급자의 것인지 확인
            boolean allSameBeneficiary = careLogs.stream()
                    .allMatch(log -> log.getBeneficiaryId().equals(beneficiaryId));
            assertThat(allSameBeneficiary).isTrue();
        }
        System.out.println("=================================");
    }

    @Test
    @DisplayName("요양일지 상세 조회: 요양일지의 상세 정보가 조회되어야 한다")
    void selectCareLogDetail_shouldReturnDetail() {
        // Given
        Long logId = 1L; // dev DB에 존재하는 요양일지 ID

        // When
        CareLogDetailDto careLogDetail = careLogMapper.selectCareLogDetail(logId);

        // Then
        System.out.println("=== 요양일지 상세 조회 ===");

        if (careLogDetail != null) {
            System.out.println("요양일지 ID: " + careLogDetail.getLogId());
            System.out.println("수급자: " + careLogDetail.getBeneficiaryName());
            System.out.println("서비스 날짜: " + careLogDetail.getServiceDate());
            System.out.println("작성자: " + careLogDetail.getCareWorkerName());
            System.out.println("특이사항: " + careLogDetail.getSpecialNote());

            assertThat(careLogDetail.getLogId()).isEqualTo(logId);
            assertThat(careLogDetail.getBeneficiaryName()).isNotNull();
            assertThat(careLogDetail.getServiceDate()).isNotNull();
            assertThat(careLogDetail.getCareWorkerName()).isNotNull();
        } else {
            System.out.println("해당 요양일지를 찾을 수 없습니다.");
        }
        System.out.println("========================");
    }
}
