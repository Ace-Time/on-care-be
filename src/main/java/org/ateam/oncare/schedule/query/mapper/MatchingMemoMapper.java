package org.ateam.oncare.schedule.query.mapper;

import java.time.LocalDate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.schedule.query.dto.MatchingMemoDto;

@Mapper
public interface MatchingMemoMapper {
    MatchingMemoDto selectMemo(@Param("matchingId") Long matchingId,
                               @Param("memoDate") LocalDate memoDate);
}