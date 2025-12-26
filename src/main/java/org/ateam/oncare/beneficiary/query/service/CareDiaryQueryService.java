package org.ateam.oncare.beneficiary.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.CareLogDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.CareLogListResponse;
import org.ateam.oncare.beneficiary.query.mapper.CareLogDetailRow;
import org.ateam.oncare.beneficiary.query.mapper.CareLogQueryMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CareDiaryQueryService {

    private final CareLogQueryMapper mapper;

    public List<CareLogListResponse> getCareLogList(Long beneficiaryId, String month) {
        return mapper.selectCareLogList(beneficiaryId, month);
    }

    public CareLogDetailResponse getCareLogDetail(Long beneficiaryId, Long logId) {
        CareLogDetailRow r = mapper.selectCareLogDetail(beneficiaryId, logId);
        if (r == null) return null;

        CareLogDetailResponse res = new CareLogDetailResponse();
        res.setLogId(r.getLogId());
        res.setBeneficiaryId(r.getBeneficiaryId());
        res.setRecordedAt(r.getRecordedAt());
        res.setServiceDate(r.getServiceDate());
        res.setStartTime(r.getStartTime());
        res.setEndTime(r.getEndTime());
        res.setServiceType(r.getServiceType());
        res.setCareWorkerId(r.getCareWorkerId());
        res.setCareWorkerName(r.getCareWorkerName());
        res.setSpecialNote(r.getSpecialNote());

        // -------------------------
        // 1) 신체활동 지원(소그룹)
        // -------------------------
        CareLogDetailResponse.Physical physical = new CareLogDetailResponse.Physical();

        List<String> meal = new ArrayList<>();
        addIfTrue(meal, r.getIsBreakfast(), "아침 식사");
        addIfTrue(meal, r.getIsLunch(), "점심 식사");
        addIfTrue(meal, r.getIsDinner(), "저녁 식사");
        addIfTrue(meal, r.getIsSnack(), "간식 섭취");
        addIfTrue(meal, r.getIsMealPrep(), "식사 준비 및 정리");

        List<String> excretion = new ArrayList<>();
        // count > 0
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

        physical.setMeal(meal);
        physical.setExcretion(excretion);
        physical.setHygiene(hygiene);
        physical.setMobility(mobility);

        res.setPhysical(physical);

        // -------------------------
        // 2) 인지 및 정서 지원
        // -------------------------
        List<String> cognitive = new ArrayList<>();
        addIfTrue(cognitive, r.getIsEmotionalTalk(), "말벗 및 위로");
        addIfTrue(cognitive, r.getIsCommunication(), "의사소통 도움");
        addIfTrue(cognitive, r.getIsCounseling(), "생활상담 지원");
        addIfTrue(cognitive, r.getIsCognitiveCare(), "인지 자극 활동 지원");
        addIfTrue(cognitive, r.getIsBehaviorCare(), "인지 행동 변화 지원");
        res.setCognitive(cognitive);

        // -------------------------
        // 3) 상태 관찰 및 특이사항
        // -------------------------
        CareLogDetailResponse.Status status = new CareLogDetailResponse.Status();

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

        List<String> etc = new ArrayList<>();
        // 현재는 etc로 따로 둘 게 없어서 비워둬도 됨
        // 필요하면 나중에 is_body_etc / is_mood_etc 등을 etc로 옮길 수도 있어.

        status.setHealth(health);
        status.setMood(mood);
        status.setSleep(sleep);
        status.setEtc(etc);

        res.setStatus(status);

        return res;
    }

    private void addIfTrue(List<String> target, Boolean v, String label) {
        if (Boolean.TRUE.equals(v)) target.add(label);
    }

    private int safeInt(Integer v) {
        return v == null ? 0 : v;
    }
}