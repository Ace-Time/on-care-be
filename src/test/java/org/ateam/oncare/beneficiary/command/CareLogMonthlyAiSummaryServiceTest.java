package org.ateam.oncare.beneficiary.command;

import org.ateam.oncare.beneficiary.query.dto.ai.MonthlySummaryAiResponse;
import org.ateam.oncare.beneficiary.query.mapper.AiCareInsertMapper;
import org.ateam.oncare.beneficiary.query.mapper.CareLogDetailRow;
import org.ateam.oncare.beneficiary.query.mapper.CareLogQueryMapper;
import org.ateam.oncare.beneficiary.query.service.ai.CareLogMonthlyAiSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CareLogMonthlyAiSummaryServiceTest {

    @Mock private CareLogQueryMapper mapper;
    @Mock private AiCareInsertMapper aiCareInsertMapper;
    @Mock private RestTemplate aiRestTemplate;

    @InjectMocks
    private CareLogMonthlyAiSummaryService service;

    @BeforeEach
    void setUp() {
        // @Value 주입 필드라 Mockito 환경에선 직접 세팅 필요
        ReflectionTestUtils.setField(service, "aiBaseUrl", "http://ai.test");
    }

    @Test
    @DisplayName("해당 월 로그가 없으면 AI 호출/DB 저장 없이 안내 메시지 반환")
    void generateMonthlySummary_whenNoRows_shouldNotCallAiNorInsert() {
        Long beneficiaryId = 1L;
        String month = "2026-01";

        when(mapper.selectCareLogDetailsByMonth(beneficiaryId, month)).thenReturn(List.of());

        MonthlySummaryAiResponse res = service.generateMonthlySummary(beneficiaryId, month);

        assertThat(res).isNotNull();
        assertThat(res.getBeneficiaryId()).isEqualTo(beneficiaryId);
        assertThat(res.getMonth()).isEqualTo(month);
        assertThat(res.getSummaryText()).contains("요양일지가 없어");
        assertThat(res.getMeta()).isNotNull();
        assertThat(res.getMeta().get("logsCount")).isEqualTo(0);

        verify(aiRestTemplate, never()).exchange(anyString(), any(), any(), eq(MonthlySummaryAiResponse.class));
        verify(aiCareInsertMapper, never()).insertAiCare(anyLong(), anyString(), anyString(),
                anyLong(), anyLong(), anyString(), anyLong(), anyLong(), anyLong());
    }

    @Test
    @DisplayName("정상 흐름: AI 응답 성공 시 summary trim되어 저장되고, lastLogId/logsCount/lastServiceDate/tokens가 계산되어 insert된다")
    void generateMonthlySummary_success_shouldInsertAiCareWithComputedMeta() {
        Long beneficiaryId = 77L;
        String month = "2026-01";

        // rows 2개: max(logId)=200, max(serviceDate)=2026-01-15, logsCount=2
        CareLogDetailRow r1 = new CareLogDetailRow();
        r1.setLogId(100L);
        r1.setServiceDate("2026-01-10");

        CareLogDetailRow r2 = new CareLogDetailRow();
        r2.setLogId(200L);
        r2.setServiceDate("2026-01-15");

        when(mapper.selectCareLogDetailsByMonth(beneficiaryId, month)).thenReturn(List.of(r1, r2));

        MonthlySummaryAiResponse body = new MonthlySummaryAiResponse();
        body.setBeneficiaryId(beneficiaryId);
        body.setMonth(month);
        body.setSummaryText("   요약 결과입니다   "); // trim 대상
        body.setMeta(Map.of(
                "inputTokens", 12,          // Number
                "outputTokens", "34",       // String
                "totalTokens", 46           // Number
        ));

        ResponseEntity<MonthlySummaryAiResponse> okResp =
                new ResponseEntity<>(body, HttpStatus.OK);

        when(aiRestTemplate.exchange(
                eq("http://ai.test/ai/summaries/monthly"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(MonthlySummaryAiResponse.class)
        )).thenReturn(okResp);

        MonthlySummaryAiResponse res = service.generateMonthlySummary(beneficiaryId, month);

        assertThat(res).isNotNull();
        assertThat(res.getSummaryText()).contains("요약"); // 원본 응답 그대로 반환(서비스가 body 리턴)

        // insert 호출 파라미터 검증
        verify(aiCareInsertMapper, times(1)).insertAiCare(
                eq(beneficiaryId),
                eq(month),
                eq("요약 결과입니다"),   // trim 확인
                eq(200L),              // lastLogId
                eq(2L),                // logsCount
                eq("2026-01-15"),       // lastServiceDate
                eq(12L),               // inputTokens
                eq(34L),               // outputTokens
                eq(46L)                // totalTokens
        );
    }

    @Test
    @DisplayName("AI summaryText가 null/blank이면 DB에 저장하지 않는다")
    void generateMonthlySummary_blankSummary_shouldNotInsert() {
        Long beneficiaryId = 1L;
        String month = "2026-01";

        CareLogDetailRow r1 = new CareLogDetailRow();
        r1.setLogId(1L);
        r1.setServiceDate("2026-01-01");

        when(mapper.selectCareLogDetailsByMonth(beneficiaryId, month)).thenReturn(List.of(r1));

        MonthlySummaryAiResponse body = new MonthlySummaryAiResponse();
        body.setBeneficiaryId(beneficiaryId);
        body.setMonth(month);
        body.setSummaryText("   "); // blank
        body.setMeta(Map.of("inputTokens", 1, "outputTokens", 1, "totalTokens", 2));

        when(aiRestTemplate.exchange(anyString(), any(), any(), eq(MonthlySummaryAiResponse.class)))
                .thenReturn(new ResponseEntity<>(body, HttpStatus.OK));

        MonthlySummaryAiResponse res = service.generateMonthlySummary(beneficiaryId, month);

        assertThat(res).isNotNull();
        verify(aiCareInsertMapper, never()).insertAiCare(anyLong(), anyString(), anyString(),
                anyLong(), anyLong(), anyString(), anyLong(), anyLong(), anyLong());
    }
}
