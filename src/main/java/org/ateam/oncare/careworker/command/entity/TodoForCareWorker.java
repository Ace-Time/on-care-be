package org.ateam.oncare.careworker.command.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "todo_for_care_worker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoForCareWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "todo", length = 2000)
    private String todo;

    @Column(name = "todo_date", nullable = false)
    private LocalDate todoDate;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "clear_st")
    @ColumnDefault("0")
    private Boolean clearSt; // 0: 미완료, 1: 완료

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    @Column(name = "beneficiary_id", nullable = false)
    private Long beneficiaryId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;
}