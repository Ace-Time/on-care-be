package org.ateam.oncare.schedule.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarDayItemDto;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarMonthCountDto;
import org.ateam.oncare.schedule.query.dto.ConfirmedCalendarScheduleDetailDto;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ConfirmedCalendarScheduleMapper {

    List<ConfirmedCalendarMonthCountDto> selectRangeCounts(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("careWorkerId") Integer careWorkerId,
            @Param("serviceTypeId") Integer serviceTypeId,
            @Param("keyword") String keyword,
            @Param("searchField") String searchField
    );

    List<ConfirmedCalendarDayItemDto> selectDayList(
            @Param("date") LocalDate date,
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("careWorkerId") Integer careWorkerId,
            @Param("serviceTypeId") Integer serviceTypeId,
            @Param("keyword") String keyword,
            @Param("searchField") String searchField
    );

    ConfirmedCalendarScheduleDetailDto selectDetailByVsId(
            @Param("vsId") Integer vsId
    );

    List<ConfirmedCalendarDayItemDto> selectDayListPaged(
            @Param("date") LocalDate date,
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("careWorkerId") Integer careWorkerId,
            @Param("serviceTypeId") Integer serviceTypeId,
            @Param("keyword") String keyword,
            @Param("searchField") String searchField,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    long countDayList(
            @Param("date") LocalDate date,
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("careWorkerId") Integer careWorkerId,
            @Param("serviceTypeId") Integer serviceTypeId,
            @Param("keyword") String keyword,
            @Param("searchField") String searchField
    );
}