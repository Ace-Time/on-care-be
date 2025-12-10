package org.ateam.oncare.careworker.command.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "care_worker_service_type")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(CareWorkerServiceTypeId.class)
public class CareWorkerServiceType {

    @Id
    @Column(name = "m_service_type_id", nullable = false)
    private Long mServiceTypeId;

    @Id
    @Column(name = "care_worker_id", nullable = false)
    private Long careWorkerId;

    // 생성자 등을 통해 객체 생성
    public CareWorkerServiceType(Long mServiceTypeId, Long careWorkerId) {
        this.mServiceTypeId = mServiceTypeId;
        this.careWorkerId = careWorkerId;
    }
}