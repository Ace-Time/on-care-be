package org.ateam.oncare.matching.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TagOverlapCountDto {
    private Long careWorkerId;
    private Integer overlapCount;
}