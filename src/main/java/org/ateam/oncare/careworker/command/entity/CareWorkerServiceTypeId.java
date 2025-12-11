package org.ateam.oncare.careworker.command.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@EqualsAndHashCode // 필수
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CareWorkerServiceTypeId implements Serializable {
    private Long mServiceTypeId;
    private Long careWorkerId;
}