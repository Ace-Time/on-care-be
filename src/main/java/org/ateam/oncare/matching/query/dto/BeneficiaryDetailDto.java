package org.ateam.oncare.matching.query.dto;

import lombok.Data;
import java.util.List;

@Data
public class BeneficiaryDetailDto {

    private Long beneficiaryId;

    private String name;        // 김영희
    private String gender;      // 남자 / 여자
    private String riskLevel;   // 저위험 / 중위험 / 고위험

    private String address;
    private String phone;

    /* =========================
       수급자 요구 조건
    ========================== */
    private Long serviceTypeId;
    private String serviceTypeName;

    /** 필요 서비스 유형 (방문요양, 방문간호 등) */
    private List<String> serviceTypes;

    /** 위험요소 (치매, 욕창, 고혈압 등 → 자격증 매칭용) */
    private List<String> riskFactors;

    /** 취향/성향 태그 (말벗, 산책, 음악 등 → 카드/점수용) */
    private List<String> tags;

    /* =========================
       일정 정보
    ========================== */

    /** 희망 요일/시간 */
    private List<BeneficiaryScheduleViewDto> schedules;

    /* =========================
       배정 정보
    ========================== */

    /** 배정된 요양보호사 (없으면 null) */
    private AssignedCareWorkerDto assignedCareWorker;
}