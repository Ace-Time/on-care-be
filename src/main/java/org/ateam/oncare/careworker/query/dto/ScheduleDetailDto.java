package org.ateam.oncare.careworker.query.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

// 일정 상세용 (Detail View)
@Getter @Setter
public class ScheduleDetailDto {
    private Long scheduleId;
    private String status;          // 배지 표시용 상태
    private String date;            // "2025-12-15"
    private String timeRange;       // "09:00-11:00"
    private String serviceContent;  // 서비스 내용
    private String specialNotes;    // 특이사항 (노란 박스)
    // 중첩 객체 (수급자 정보)
    private RecipientInfoDto recipient;
    // 1:N 관계 리스트 (MyBatis Collection 매핑 대상)
    private List<String> riskFactors;   // 위험요소 태그 목록
    private List<String> personalTags;  // 개인 태그 목록
    private List<String> significants;  // 특이사항 목록
}