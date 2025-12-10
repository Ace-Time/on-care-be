package org.ateam.oncare.beneficiary.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "care_service_count")
public class CareServiceCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 5)
    private String name;

    @Column(name = "money", nullable = false)
    private Integer money;

    // --- 외래키 ID 직접 매핑 ---

    @Column(name = "m_care_level_id", nullable = false)
    private Integer mCareLevelId;
}