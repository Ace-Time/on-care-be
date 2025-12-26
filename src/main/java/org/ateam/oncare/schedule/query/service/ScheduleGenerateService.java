package org.ateam.oncare.schedule.query.service;

import java.time.LocalDate;
import java.time.YearMonth;

import org.ateam.oncare.schedule.query.mapper.VisitScheduleGenerateMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleGenerateService {

    private final VisitScheduleGenerateMapper visitScheduleGenerateMapper;

    @Transactional
    public int generateNextMonthVisitSchedules() {
        YearMonth nextMonth = YearMonth.now().plusMonths(1);

        LocalDate startDate = nextMonth.atDay(1);
        LocalDate endDate = nextMonth.atEndOfMonth();

        return visitScheduleGenerateMapper.insertNextMonthVisitSchedules(startDate, endDate);
    }
}