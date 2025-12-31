package org.ateam.oncare.counsel.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "potential_stage")
public class PotentialStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "stage", nullable = false)
    private Integer stage; // 1, 2, 3, 4

    @Column(name = "process_status", nullable = false, length = 1)
    private String processStatus; // "P" or "F"

    @Column(name = "process_time")
    private LocalDateTime processTime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Map -> JSON 변환 위해 @JdbcTypeCode 사용
    @Column(name = "stage_data", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> stageData;

    @Column(name = "potential_customer_id", nullable = false)
    private Long potentialCustomerId;

    public void updateStageData(Map<String, Object> stageData, String newProcessStatus) {
        this.stageData = stageData;
        if (!newProcessStatus.equals(this.processStatus)) {
            this.processStatus = newProcessStatus;
            this.processTime = LocalDateTime.now();
        }
    }
}