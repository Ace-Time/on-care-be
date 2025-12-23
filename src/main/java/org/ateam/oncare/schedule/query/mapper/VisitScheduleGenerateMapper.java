package org.ateam.oncare.schedule.query.mapper;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VisitScheduleGenerateMapper {

    int insertNextMonthVisitSchedules(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}