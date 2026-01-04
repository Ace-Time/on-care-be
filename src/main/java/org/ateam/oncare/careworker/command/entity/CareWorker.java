package org.ateam.oncare.careworker.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "care_worker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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