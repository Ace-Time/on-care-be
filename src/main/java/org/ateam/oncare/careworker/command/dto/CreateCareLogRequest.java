package org.ateam.oncare.careworker.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCareLogRequest {
    private Long vsId;
    private LocalDate serviceDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String serviceType;
    private Long beneficiaryId;

    // 식사
    private Boolean isBreakfast;
    private Boolean isLunch;
    private Boolean isDinner;
    private Boolean isSnack;

    // 배설
    private Integer diaperCount;
    private Integer toiletCount;
    private Boolean isPortableToilet;
    private Boolean isUrine;
    private Boolean isStool;
    private Boolean stoolNormal;
    private Boolean stoolDiarrhea;
    private Boolean stoolConstipation;
    private Boolean isExcretionMistake;

    // 위생
    private Boolean isFaceWash;
    private Boolean isOralCare;
    private Boolean isHairWash;
    private Boolean isBodyWash;
    private Boolean isChangeClothes;

    // 생활
    private Boolean isMealPrep;
    private Boolean isBedCare;
    private Boolean isPositionChange;
    private Boolean isGetUpHelp;
    private Boolean isIndoorMove;
    private Boolean isWalkHelp;

    // 정서/인지
    private Boolean isEmotionalTalk;
    private Boolean isCommunication;
    private Boolean isCounseling;
    private Boolean isCognitiveCare;
    private Boolean isBehaviorCare;

    // 건강
    private Boolean isHealthGood;
    private Boolean isPain;
    private Boolean isEdema;
    private Boolean isSkinIssue;
    private Boolean isBodyEtc;

    // 기분
    private Boolean isMoodCalm;
    private Boolean isMoodAnxious;
    private Boolean isMoodDepressed;
    private Boolean isMoodAngry;
    private Boolean isMoodEtc;

    // 수면
    private Boolean isSleepLack;
    private Boolean isNapExcess;

    // 특이사항
    private String specialNote;
}
