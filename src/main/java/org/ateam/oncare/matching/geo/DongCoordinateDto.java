package org.ateam.oncare.matching.geo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DongCoordinateDto {

    private String sidoName;
    private String guName;
    private String dongName;
    private double lat;
    private double lng;
}