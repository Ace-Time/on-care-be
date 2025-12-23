package org.ateam.oncare.matching.query.dto;

import lombok.Data;
import java.util.List;

@Data
public class CareWorkerCardDto {
    private Long careWorkerId;
    private String name;     // employee.name
    private String gender;   // employee.gender
    private List<String> tags; // personal_tag.tag
}