package org.ateam.oncare.careworker.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryDetailDto {

    // 기본 정보
    private Long beneficiaryId;
    private String name;
    private String gender;
    private LocalDate birthdate;
    private Integer age;
    private String address;
    private String phone;

    // 장기요양등급 정보
    private String careLevel;
    private LocalDate careLevelStartDate;
    private LocalDate careLevelEndDate;
    private String careLevelNumber;

    // 보호자 정보
    private List<GuardianDto> guardians;

    // 위험요소
    private List<String> riskFactors;

    // 태그
    private List<String> tags;

    // 특이사항
    private List<String> significants;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GuardianDto {
        private String name;
        private String phone;
        private String relation;
        private Boolean isPrimary;
    }
}
