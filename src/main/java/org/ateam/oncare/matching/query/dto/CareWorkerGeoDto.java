package org.ateam.oncare.matching.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CareWorkerGeoDto {
    private Long careWorkerId;
    private String address;
    private Double lat;
    private Double lng;
    private Integer geoReady; // 0/1
}