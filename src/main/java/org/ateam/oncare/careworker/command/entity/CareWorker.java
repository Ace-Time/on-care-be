package org.ateam.oncare.careworker.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "care_worker")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CareWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Builder
    public CareWorker(Long employeeId) {
        this.employeeId = employeeId;
    }
}