package org.ateam.oncare.matching.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CareWorkerLatLngDto {
    private Long careWorkerId;
    private Double lat;
    private Double lng;
}