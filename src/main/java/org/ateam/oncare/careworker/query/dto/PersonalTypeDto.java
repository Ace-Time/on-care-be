package org.ateam.oncare.careworker.query.dto;

import lombok.Getter;
import lombok.Setter;

// 개인 일정 유형 (드롭다운용)
@Getter @Setter
public class PersonalTypeDto {
    private Integer id;
    private String name;        // 점심, 휴식, 휴가, 병원, 교육, 회의, 개인용무, 기타
    private String description;
}
