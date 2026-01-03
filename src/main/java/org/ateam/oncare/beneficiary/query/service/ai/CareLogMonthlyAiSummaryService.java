package org.ateam.oncare.beneficiary.query.service.ai;

// DB -> (칩 변환) -> FastAPI 호출

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.ai.MonthlySummaryAiRequest;
import org.ateam.oncare.beneficiary.query.dto.ai.MonthlySummaryAiResponse;
import org.ateam.oncare.beneficiary.query.mapper.AiCareInsertMapper;
import org.ateam.oncare.beneficiary.query.mapper.CareLogDetailRow;
import org.ateam.oncare.beneficiary.query.mapper.CareLogQueryMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CareLogMonthlyAiSummaryService {

    private final CareLogQueryMapper mapper;
    private final AiCareInsertMapper aiCareInsertMapper;
    private final RestTemplate aiRestTemplate;

    @Value("${ai.base-url}")
    private String aiBaseUrl;

    public MonthlySummaryAiResponse generateMonthlySummary(Long beneficiaryId, String month) {
        // 1) DB에서 해당 월 전체 상세 rows 조회
        List<CareLogDetailRow> rows = mapper.selectCareLogDetailsByMonth(beneficiaryId, month);

        // 추가: 해당 월 로그가 없으면 AI 호출하지 않고 바로 반환
        if (rows == null || rows.isEmpty()) {
            MonthlySummaryAiResponse res = new MonthlySummaryAiResponse();
            res.setBeneficiaryId(beneficiaryId);
            res.setMonth(month);
            res.setSummaryText("해당 월에 요양일지가 없어 요약을 생성할 수 없습니다.");
            res.setMeta(Map.of("logsCount", 0, "model", "none"));
            return res;
        }

        // 2) FastAPI 요청 바디 구성
        MonthlySummaryAiRequest req = MonthlySummaryAiRequest.builder()
                .beneficiaryId(beneficiaryId)
                .month(month)
                .logs(toDailyLogs(rows))
                .build();

        // 3) FastAPI 호출
        String url = aiBaseUrl + "/ai/summaries/monthly";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<MonthlySummaryAiRequest> entity = new HttpEntity<>(req, headers);

        ResponseEntity<MonthlySummaryAiResponse> resp = aiRestTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                MonthlySummaryAiResponse.class
        );

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new RuntimeException("AI 요약 서버 응답이 비정상입니다. status=" + resp.getStatusCode());
        }

        MonthlySummaryAiResponse body = resp.getBody();


        // meta에서 토큰 꺼내기
        Long inputTokens = getLongFromMeta(body.getMeta(), "inputTokens");
        Long outputTokens = getLongFromMeta(body.getMeta(), "outputTokens");
        Long totalTokens = getLongFromMeta(body.getMeta(), "totalTokens");

        //  4) AI 요약 성공 시 ai_care에 "이력" INSERT
        // - ai_last_log_id: rows 중 max(logId)
        // - ai_logs_count: rows.size()
        // - ai_last_service_date: rows 중 max(serviceDate) (YYYY-MM-DD)
        // 주의: DB 컬럼이 NOT NULL이라 값이 반드시 들어가야 함
        String summaryText = body.getSummaryText();

        // summaryText가 null/빈값이면 저장 안 하는 방어 (원하면 제거해도 됨)
        if (summaryText != null && !summaryText.trim().isEmpty()) {
            Long lastLogId = calcMaxLogId(rows);
            Long logsCount = (long) rows.size();
            String lastServiceDate = calcMaxServiceDate(rows);

            // lastLogId / lastServiceDate는 rows가 비어있지 않으니 null이면 안 되는 게 정상
            if (lastLogId == null || lastServiceDate == null) {
                throw new RuntimeException("AI 요약 저장용 메타 계산 실패(lastLogId/lastServiceDate가 null)");
            }

            aiCareInsertMapper.insertAiCare(
                    beneficiaryId,
                    month,
                    summaryText.trim(),
                    lastLogId,
                    logsCount,
                    lastServiceDate,
                    inputTokens,
                    outputTokens,
                    totalTokens
            );
        }

        return body;
    }

    // 아래 헬퍼 메서드 추가(토큰 관련)
    private Long getLongFromMeta(Map<String, Object> meta, String key) {
        if (meta == null) return 0L;
        Object v = meta.get(key);
        if (v == null) return 0L;
        if (v instanceof Number) return ((Number) v).longValue();
        try {
            return Long.parseLong(String.valueOf(v));
        } catch (Exception e) {
            return 0L;
        }
    }

    private Long calcMaxLogId(List<CareLogDetailRow> rows) {
        Long max = null;
        for (CareLogDetailRow r : rows) {
            if (r == null || r.getLogId() == null) continue;
            if (max == null || r.getLogId() > max) max = r.getLogId();
        }
        return max;
    }

    private String calcMaxServiceDate(List<CareLogDetailRow> rows) {
        // serviceDate 포맷이 YYYY-MM-DD 라고 너 XML에서 DATE_FORMAT으로 보장됨
        // 문자열 비교로도 날짜 비교가 가능(사전순 == 날짜순)
        String max = null;
        for (CareLogDetailRow r : rows) {
            if (r == null || r.getServiceDate() == null) continue;
            String d = r.getServiceDate();
            if (max == null || d.compareTo(max) > 0) max = d;
        }
        return max;
    }

    private List<MonthlySummaryAiRequest.DailyLogForSummary> toDailyLogs(List<CareLogDetailRow> rows) {
        List<MonthlySummaryAiRequest.DailyLogForSummary> out = new ArrayList<>();

        for (CareLogDetailRow r : rows) {
            List<String> meal = new ArrayList<>();
            addIfTrue(meal, r.getIsBreakfast(), "아침 식사");
            addIfTrue(meal, r.getIsLunch(), "점심 식사");
            addIfTrue(meal, r.getIsDinner(), "저녁 식사");
            addIfTrue(meal, r.getIsSnack(), "간식 섭취");
            addIfTrue(meal, r.getIsMealPrep(), "식사 준비 및 정리");

            List<String> excretion = new ArrayList<>();
            if (safeInt(r.getDiaperCount()) > 0) excretion.add("기저귀 교체 " + r.getDiaperCount() + "회");
            if (safeInt(r.getToiletCount()) > 0) excretion.add("화장실 이용 " + r.getToiletCount() + "회");
            addIfTrue(excretion, r.getIsPortableToilet(), "이동식 변기 사용");
            addIfTrue(excretion, r.getIsUrine(), "배뇨");
            addIfTrue(excretion, r.getIsStool(), "배변");
            addIfTrue(excretion, r.getStoolNormal(), "정상 변");
            addIfTrue(excretion, r.getStoolDiarrhea(), "설사");
            addIfTrue(excretion, r.getStoolConstipation(), "변비");
            addIfTrue(excretion, r.getIsExcretionMistake(), "배뇨/배변 실수");

            List<String> hygiene = new ArrayList<>();
            addIfTrue(hygiene, r.getIsFaceWash(), "세면 도움");
            addIfTrue(hygiene, r.getIsOralCare(), "구강 관리");
            addIfTrue(hygiene, r.getIsHairWash(), "머리 감기");
            addIfTrue(hygiene, r.getIsBodyWash(), "몸 씻기");
            addIfTrue(hygiene, r.getIsChangeClothes(), "옷 갈아입기");

            List<String> mobility = new ArrayList<>();
            addIfTrue(mobility, r.getIsPositionChange(), "체위 변경");
            addIfTrue(mobility, r.getIsGetUpHelp(), "일어나앉기 도움");
            addIfTrue(mobility, r.getIsIndoorMove(), "실내 이동");
            addIfTrue(mobility, r.getIsWalkHelp(), "산책 및 보행 도움");
            addIfTrue(mobility, r.getIsBedCare(), "잠자리 정돈");

            List<String> cognitive = new ArrayList<>();
            addIfTrue(cognitive, r.getIsEmotionalTalk(), "말벗 및 위로");
            addIfTrue(cognitive, r.getIsCommunication(), "의사소통 도움");
            addIfTrue(cognitive, r.getIsCounseling(), "생활상담 지원");
            addIfTrue(cognitive, r.getIsCognitiveCare(), "인지 자극 활동 지원");
            addIfTrue(cognitive, r.getIsBehaviorCare(), "인지 행동 변화 지원");

            List<String> health = new ArrayList<>();
            addIfTrue(health, r.getIsHealthGood(), "건강 양호");
            addIfTrue(health, r.getIsPain(), "통증");
            addIfTrue(health, r.getIsEdema(), "부종");
            addIfTrue(health, r.getIsSkinIssue(), "피부 문제");
            addIfTrue(health, r.getIsBodyEtc(), "기타 신체 이상");

            List<String> mood = new ArrayList<>();
            addIfTrue(mood, r.getIsMoodCalm(), "평온");
            addIfTrue(mood, r.getIsMoodAnxious(), "불안");
            addIfTrue(mood, r.getIsMoodDepressed(), "우울");
            addIfTrue(mood, r.getIsMoodAngry(), "분노");
            addIfTrue(mood, r.getIsMoodEtc(), "기타 감정");

            List<String> sleep = new ArrayList<>();
            addIfTrue(sleep, r.getIsSleepLack(), "수면 부족");
            addIfTrue(sleep, r.getIsNapExcess(), "낮잠 과다");

            String note = truncateNote(r.getSpecialNote());

            out.add(MonthlySummaryAiRequest.DailyLogForSummary.builder()
                    .date(r.getServiceDate()) // 월별 요약은 service_date 기준이 자연스러움
                    .physicalMeal(meal)
                    .physicalExcretion(excretion)
                    .physicalHygiene(hygiene)
                    .physicalMobility(mobility)
                    .cognitive(cognitive)
                    .statusHealth(health)
                    .statusMood(mood)
                    .statusSleep(sleep)
                    .specialNote(note)
                    .build());
        }

        return out;
    }

    // 해당 컬럼이 1(true)이면 label이 리스트에 추가
    private void addIfTrue(List<String> target, Boolean v, String label) {
        if (Boolean.TRUE.equals(v)) target.add(label);
    }

    // 조건부 라벨 (기저귀 교체, 화장실 이용에 활용)
    private int safeInt(Integer v) {
        return v == null ? 0 : v;
    }

    // 특이사항 글자 수
    private String truncateNote(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        return t.length() > 120 ? t.substring(0, 120) + "…" : t;
    }
}
