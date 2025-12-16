package org.ateam.oncare.schedule.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.schedule.query.dto.ScheduleDayItemDto;
import org.ateam.oncare.schedule.query.dto.ScheduleMonthCountDto;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ScheduleQueryMapper {

    List<ScheduleMonthCountDto> selectRangeCounts(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("careWorkerId") Integer careWorkerId,
            @Param("serviceTypeId") Integer serviceTypeId,
            @Param("keyword") String keyword,
            @Param("searchField") String searchField
    );

    List<ScheduleDayItemDto> selectDaySchedules(
            @Param("date") LocalDate date,
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("careWorkerId") Integer careWorkerId,
            @Param("serviceTypeId") Integer serviceTypeId,
            @Param("keyword") String keyword,
            @Param("searchField") String searchField
    );
}