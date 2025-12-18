package org.ateam.oncare.counsel.command.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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

    @Column(name = "month", nullable = false)
    private LocalDateTime month;

    @Lob // TEXT 타입 매핑
    @Column(name = "html_code", nullable = false, columnDefinition = "TEXT")
    private String htmlCode;

    // [변경됨] 객체 참조(@ManyToOne) 대신 ID 값 직접 매핑
    @Column(name = "potential_customer_id", nullable = false)
    private Long potentialCustomerId;
}