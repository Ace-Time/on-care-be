package org.ateam.oncare.careworker.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "basic_evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicEvaluations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eval_id")
    private Long evalId;

    /**
     * 평가 유형 (FALL, BEDSORE, COGNITIVE, NEEDS)
     * Enum으로 관리하여 데이터 무결성 보장
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "eval_type", nullable = false, length = 20)
    private EvaluationType evalType;

    @Column(name = "template_id", nullable = false)
    private Long templateId;

    @Column(name = "eval_date", nullable = false)
    private LocalDate evalDate;

    /**
     * JSON 데이터 처리
     * Hibernate 6 이상에서는 @JdbcTypeCode(SqlTypes.JSON) 사용 권장
     * 단순 문자열로 처리하려면 String 타입 사용
     */
    @Column(name = "eval_data", nullable = false, columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private String evalData; // 혹은 Map<String, Object> evalData;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;

    @Column(name = "care_worker_id", nullable = false)
    private Long careworkerId;

    @Column(name = "is_draft", nullable = false)
    private Boolean isDraft = false;  // 임시저장 여부

    // 내부 Enum 정의
    public enum EvaluationType {
        FALL,      // 낙상
        BEDSORE,   // 욕창
        COGNITIVE, // 인지
        NEEDS      // 욕구
    }
}