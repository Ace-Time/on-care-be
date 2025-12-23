package org.ateam.oncare.beneficiary.query.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.query.mapper.CalendarFixedVisitRow;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BeneficiaryScheduleCalendarMapper {

    List<CalendarFixedVisitRow> selectFixedVisitsInRange(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("startDt") LocalDateTime startDt,
            @Param("endDt") LocalDateTime endDt
    );
}