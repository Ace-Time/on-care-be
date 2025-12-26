package org.ateam.oncare.schedule.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VisitScheduleGenerateScheduler {

    private final ScheduleGenerateService scheduleGenerateService;

    @Scheduled(cron = "0 0 0 25 * *", zone = "Asia/Seoul")
    public void run() {
        int inserted = scheduleGenerateService.generateNextMonthVisitSchedules();
        log.info("visit_schedule nextMonth generated. inserted={}", inserted);
    }
}