package org.ateam.oncare.matching.query.dto;

import lombok.Data;
import java.util.List;

@Data
public class CareWorkerCardDto {
    private Long careWorkerId;
    private String name;     // employee.name
    private String gender;   // employee.gender
    private List<String> tags; // personal_tag.tag

    private Integer overlapCount;   // 태그 겹침 수
    private Integer tagScore;       // overlapCount * 2
    private Double distanceKm;      // km (좌표 없으면 null)
    private Integer distanceScore;  // 좌표 없으면 null
    private Integer totalScore;     // 좌표 없으면 null
}