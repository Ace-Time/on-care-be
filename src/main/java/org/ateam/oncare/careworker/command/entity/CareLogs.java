// 요양 일지
package org.ateam.oncare.careworker.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "care_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "vs_id", nullable = false)
    private Long vsId;

    @Column(name = "service_date", nullable = false)
    private LocalDate serviceDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "service_type", nullable = false, length = 20)
    private String serviceType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    // --- 식사 보조 ---
    @Column(name = "is_breakfast", nullable = false) private Boolean isBreakfast;
    @Column(name = "is_lunch", nullable = false) private Boolean isLunch;
    @Column(name = "is_dinner", nullable = false) private Boolean isDinner;
    @Column(name = "is_snack", nullable = false) private Boolean isSnack;

    // --- 배설 보조 ---
    @Column(name = "diaper_count", nullable = false) private Integer diaperCount;
    @Column(name = "toilet_count", nullable = false) private Integer toiletCount;
    @Column(name = "is_portable_toilet", nullable = false) private Boolean isPortableToilet;
    @Column(name = "is_urine", nullable = false) private Boolean isUrine;
    @Column(name = "is_stool", nullable = false) private Boolean isStool;
    @Column(name = "stool_normal", nullable = false) private Boolean stoolNormal;
    @Column(name = "stool_diarrhea", nullable = false) private Boolean stoolDiarrhea;
    @Column(name = "stool_constipation", nullable = false) private Boolean stoolConstipation;
    @Column(name = "is_excretion_mistake", nullable = false) private Boolean isExcretionMistake;

    // --- 위생 관리 ---
    @Column(name = "is_face_wash", nullable = false) private Boolean isFaceWash;
    @Column(name = "is_oral_care", nullable = false) private Boolean isOralCare;
    @Column(name = "is_hair_wash", nullable = false) private Boolean isHairWash;
    @Column(name = "is_body_wash", nullable = false) private Boolean isBodyWash;
    @Column(name = "is_change_clothes", nullable = false) private Boolean isChangeClothes;

    // --- 생활 지원 ---
    @Column(name = "is_meal_prep", nullable = false) private Boolean isMealPrep;
    @Column(name = "is_bed_care", nullable = false) private Boolean isBedCare;
    @Column(name = "is_position_change", nullable = false) private Boolean isPositionChange;
    @Column(name = "is_get_up_help", nullable = false) private Boolean isGetUpHelp;
    @Column(name = "is_indoor_move", nullable = false) private Boolean isIndoorMove;
    @Column(name = "is_walk_help", nullable = false) private Boolean isWalkHelp;

    // --- 정서/인지 ---
    @Column(name = "is_emotional_talk", nullable = false) private Boolean isEmotionalTalk;
    @Column(name = "is_communication", nullable = false) private Boolean isCommunication;
    @Column(name = "is_counseling", nullable = false) private Boolean isCounseling;
    @Column(name = "is_cognitive_care", nullable = false) private Boolean isCognitiveCare;
    @Column(name = "is_behavior_care", nullable = false) private Boolean isBehaviorCare;

    // --- 건강 상태 ---
    @Column(name = "is_health_good", nullable = false) private Boolean isHealthGood;
    @Column(name = "is_pain", nullable = false) private Boolean isPain;
    @Column(name = "is_edema", nullable = false) private Boolean isEdema;
    @Column(name = "is_skin_issue", nullable = false) private Boolean isSkinIssue;
    @Column(name = "is_body_etc", nullable = false) private Boolean isBodyEtc;

    // --- 기분 상태 ---
    @Column(name = "is_mood_calm", nullable = false) private Boolean isMoodCalm;
    @Column(name = "is_mood_anxious", nullable = false) private Boolean isMoodAnxious;
    @Column(name = "is_mood_depressed", nullable = false) private Boolean isMoodDepressed;
    @Column(name = "is_mood_angry", nullable = false) private Boolean isMoodAngry;
    @Column(name = "is_mood_etc", nullable = false) private Boolean isMoodEtc;

    // --- 수면 ---
    @Column(name = "is_sleep_lack", nullable = false) private Boolean isSleepLack;
    @Column(name = "is_nap_excess", nullable = false) private Boolean isNapExcess;

    @Lob
    @Column(name = "special_note")
    private String specialNote;

    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;
}