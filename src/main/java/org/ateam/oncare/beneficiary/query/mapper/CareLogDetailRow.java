package org.ateam.oncare.beneficiary.query.mapper;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CareLogDetailRow {

    private Long logId;
    private Long beneficiaryId;

    private String recordedAt;
    private String serviceDate;
    private String startTime;
    private String endTime;
    private String serviceType;

    private Integer careWorkerId;
    private String careWorkerName;

    // 식사/영양
    private Boolean isBreakfast;
    private Boolean isLunch;
    private Boolean isDinner;
    private Boolean isSnack;
    private Boolean isMealPrep;

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

    // 이동/체위
    private Boolean isPositionChange;
    private Boolean isGetUpHelp;
    private Boolean isIndoorMove;
    private Boolean isWalkHelp;
    private Boolean isBedCare;

    // 인지/정서
    private Boolean isEmotionalTalk;
    private Boolean isCommunication;
    private Boolean isCounseling;
    private Boolean isCognitiveCare;
    private Boolean isBehaviorCare;

    // 상태(신체)
    private Boolean isHealthGood;
    private Boolean isPain;
    private Boolean isEdema;
    private Boolean isSkinIssue;
    private Boolean isBodyEtc;

    // 상태(기분)
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