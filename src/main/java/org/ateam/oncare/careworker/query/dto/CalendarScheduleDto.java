package org.ateam.oncare.careworker.query.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

// 캘린더 목록용 (List View)
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)  // null 값은 JSON에서 제외
public class CalendarScheduleDto {
    private Long scheduleId;

    @JsonProperty("isPersonal")
    private Boolean isPersonal;     // 개인 일정 여부 (Boolean 타입으로 변경)

    // 공통 필드
    private LocalDate date;         // 날짜
    private LocalTime startTime;    // 시작 시간
    private LocalTime endTime;      // 종료 시간
    private String title;           // 일정 제목 (개인일정=입력값, 수급자일정=수급자명)
    private String type;            // 일정 유형 (개인=점심/휴식 등, 수급자=방문요양 등)
    private String location;        // 장소/주소
    private String notes;           // 특이사항
    private String status;          // 상태 (SCHEDULED, IN_PROGRESS, DONE)

    // 수급자 일정 전용
    private String recipientName;   // 수급자 이름 (개인일정이면 null)
}