package org.ateam.oncare.careworker.query.dto;

import lombok.Getter;
import lombok.Setter;

// 수급자 정보 (상세 DTO 내부용)
@Getter @Setter
public class RecipientInfoDto {
    private Long recipientId;
    private String name;
    private String careGrade;       // 등급
    private String address;
    private String guardianName;    // 보호자 이름
    private String guardianPhone;   // 보호자 연락처
    private String relation;        // 보호자 관계
}