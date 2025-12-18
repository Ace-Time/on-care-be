package org.ateam.oncare.schedule.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.schedule.query.dto.ConfirmedScheduleMemoDto;

@Mapper
public interface ConfirmedScheduleMemoMapper {
    ConfirmedScheduleMemoDto selectMemoByVsId(@Param("vsId") Integer vsId);
}