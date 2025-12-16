package org.ateam.oncare.careworker.query.service;

import org.ateam.oncare.careworker.query.dto.CalendarScheduleDto;
import org.ateam.oncare.careworker.query.dto.PersonalTypeDto;
import org.ateam.oncare.careworker.query.dto.ScheduleDetailDto;
import org.ateam.oncare.careworker.query.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleQueryService {

    private final ScheduleMapper scheduleMapper;

    // 1. 기간별 조회
    public List<CalendarScheduleDto> getSchedules(Long caregiverId, LocalDate startDate, LocalDate endDate) {
        return scheduleMapper.selectSchedulesByPeriod(caregiverId, startDate, endDate);
    }

    // 2. 상세 조회
    public ScheduleDetailDto getScheduleDetail(Long scheduleId, Long caregiverId) {
        return scheduleMapper.selectScheduleDetail(scheduleId, caregiverId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없거나 접근 권한이 없습니다."));
    }

    // 3. 개인 일정 유형 목록 조회 (드롭다운용)
    public List<PersonalTypeDto> getPersonalTypes() {
        return scheduleMapper.selectPersonalTypes();
    }
}