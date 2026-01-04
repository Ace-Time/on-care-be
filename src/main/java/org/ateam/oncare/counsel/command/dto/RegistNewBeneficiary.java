package org.ateam.oncare.counsel.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistNewBeneficiary {

    // 기본 정보 (Beneficiary)
    private String name;
    private String gender;  // "M" or "F"
    private LocalDate birthdate;
    private String address;
    private String phone;
    private Long potentialCustomerId;  // 잠재고객 ID

    // 보호자 정보
    private String guardianName;
    private String guardianRelation;
    private String guardianPhone;

    // 요양등급 정보 (BeneficiaryCareLevel)
    private String level;  // "1등급", "2등급", etc.
    private String careLevelNumber;  // 인정번호
    private LocalDate careLevelStartDate;
    private LocalDate careLevelEndDate;

    // 계약 정보 (BeneficiaryHistory)
    private LocalDate contractStartDate;  // 계약 시작일
    private LocalDate contractEndDate;    // 계약 만료일

    // 태그/위험요소 (3단계 데이터)
    private List<String> selectedMatchTags;     // 매칭 태그
    private List<String> selectedRisks;         // 위험 요소

    // 스케줄 (BeneficiarySchedule)
    private List<BeneficiaryScheduleDto> beneficiarySchedules;

    // 특이사항 (3단계에서 Y로 체크된 항목들)
    // 렌탈성사도움
    private String 렌탈금액민감;
    private String 보호자결정의존;
    private String 보편상품신뢰;
    private String 거동불편;
    private String 목욕불편;

    // 문의해결도움
    private String 문자소통형;
    private String 정기연락중시형;

    // 컴플레인해결도움
    private String 요양보호사고정선호;
    private String 성격민감도높음;
    private String 금액민감도높음;

    // 해지상담도움
    private String 금액부담;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BeneficiaryScheduleDto {
        private String beneficiaryScheduleDay;       // "월", "화", etc.
        private String serviceType;
        private String beneficiaryScheduleStartTime; // "09:00"
        private String beneficiaryScheduleEndTime;   // "12:00"
    }
}
