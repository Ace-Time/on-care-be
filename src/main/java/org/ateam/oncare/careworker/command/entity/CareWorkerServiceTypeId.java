package org.ateam.oncare.careworker.command.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // 필수
public class CareWorkerServiceTypeId implements Serializable {
    private Long mServiceTypeId;
    private Long careWorkerId;
}