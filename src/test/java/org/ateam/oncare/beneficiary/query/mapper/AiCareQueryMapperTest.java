package org.ateam.oncare.beneficiary.query.mapper;

import org.ateam.oncare.beneficiary.query.dto.response.AiCareSummaryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional  //이 생성자때문에 실제 db에는 안들어감
class AiCareQueryMapperTest {

    @Autowired
    private AiCareInsertMapper aiCareInsertMapper;

    @Autowired
    private AiCareQueryMapper aiCareQueryMapper;

    @Test
    @DisplayName("같은 월에 일일요양일지가 추가되어 재요약되면, ai_id DESC 기준 최신 요약 1건이 조회된다")
    void selectLatestAiSummary_shouldReturnLatestSummary_whenSameMonthReSummarized() {
        // Given
        Long beneficiaryId = 1L;        // 1번 수급자 = 김영수
        String month = "2026-01";

        // 최초 월 요약 (예: 1~10일 일지 반영)
        int firstInsert = aiCareInsertMapper.insertAiCare(
                beneficiaryId,
                month,
                "2026-01 최초 요약(1~10일 반영)",
                100L,
                10L,
                "2026-01-10",
                100L,
                200L,
                300L
        );
        assertThat(firstInsert).isEqualTo(1);

        // 같은 월에 일지 추가 → 재요약 (예: 1~15일 반영)
        int secondInsert = aiCareInsertMapper.insertAiCare(
                beneficiaryId,
                month,
                "2026-01 재요약(1~15일 반영) - 최신",
                200L,
                20L,
                "2026-01-15",
                200L,
                300L,
                400L
        );
        assertThat(secondInsert).isEqualTo(1);

        // When
        AiCareSummaryResponse latest =
                aiCareQueryMapper.selectLatestAiSummary(beneficiaryId, month);  // 조회

        // Then
        assertThat(latest).isNotNull();

        // 콘솔에 결과 출력 (테스트 성공해도 보임)
        System.out.println("========== AI CARE SUMMARY TEST ==========");
        System.out.println("aiId              = " + latest.getAiId());
        System.out.println("beneficiaryId     = " + latest.getBeneficiaryId());
        System.out.println("month             = " + latest.getMonth());
        System.out.println("summaryText       = " + latest.getSummaryText());
        System.out.println("lastLogId         = " + latest.getLastLogId());
        System.out.println("logsCount         = " + latest.getLogsCount());
        System.out.println("lastServiceDate   = " + latest.getLastServiceDate());
        System.out.println("inputTokens       = " + latest.getInputTokens());
        System.out.println("outputTokens      = " + latest.getOutputTokens());
        System.out.println("totalTokens       = " + latest.getTotalTokens());
        System.out.println("createdAt         = " + latest.getCreatedAt());
        System.out.println("==========================================");

        // 기본 식별 정보
        assertThat(latest.getBeneficiaryId()).isEqualTo(beneficiaryId);
        assertThat(latest.getMonth()).isEqualTo(month);

        // 최신 판단 기준: ai_id DESC → 두 번째 insert 값이 나와야 함
        assertThat(latest.getSummaryText())
                .as("같은 월에서 재요약이 발생하면 최신 요약이 조회되어야 한다")
                .isEqualTo("2026-01 재요약(1~15일 반영) - 최신");

        assertThat(latest.getLastLogId()).isEqualTo(200L);
        assertThat(latest.getLogsCount()).isEqualTo(20L);
        assertThat(latest.getLastServiceDate()).isEqualTo("2026-01-15");

        // 토큰 정보도 최신 값인지 확인
        assertThat(latest.getInputTokens()).isEqualTo(200L);
        assertThat(latest.getOutputTokens()).isEqualTo(300L);
        assertThat(latest.getTotalTokens()).isEqualTo(400L);

        // DB에서 생성되는 값은 존재성만 체크
        assertThat(latest.getAiId()).isNotNull();
        assertThat(latest.getCreatedAt()).isNotBlank();
    }

    @Test
    @DisplayName("해당 수급자+월에 AI 요약 이력이 없으면 null을 반환한다")
    void selectLatestAiSummary_whenNoData_shouldReturnNull() {
        // Given
        Long beneficiaryId = 999999L;
        String month = "2099-12";

        // When
        AiCareSummaryResponse result =
                aiCareQueryMapper.selectLatestAiSummary(beneficiaryId, month);

        // Then
        System.out.println("========== AI CARE SUMMARY TEST (NO DATA) ==========");
        System.out.println("beneficiaryId = " + beneficiaryId);
        System.out.println("month         = " + month);
        System.out.println("result        = " + result);
        System.out.println("===================================================");

        assertThat(result).isNull();
    }
}
