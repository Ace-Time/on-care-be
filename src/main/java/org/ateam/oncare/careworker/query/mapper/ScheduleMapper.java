package org.ateam.oncare.careworker.query.mapper;

import org.ateam.oncare.careworker.query.dto.CalendarScheduleDto;
import org.ateam.oncare.careworker.query.dto.PersonalTypeDto;
import org.ateam.oncare.careworker.query.dto.ScheduleDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Mapper
public interface ScheduleMapper {

    // 1. 기간별 일정 조회 (캘린더용)
    List<CalendarScheduleDto> selectSchedulesByPeriod(
            @Param("caregiverId") Long caregiverId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 2. 일정 상세 조회 (1:N 매핑 필요)
    Optional<ScheduleDetailDto> selectScheduleDetail(
            @Param("scheduleId") Long scheduleId,
            @Param("caregiverId") Long caregiverId // 본인 일정인지 검증용
    );

    // 3. 개인 일정 유형 목록 조회 (드롭다운용)
    List<PersonalTypeDto> selectPersonalTypes();
}