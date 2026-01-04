package org.ateam.oncare.careworker.command.mapper;

import org.ateam.oncare.careworker.command.dto.CreatePersonalScheduleRequest;
import org.ateam.oncare.careworker.command.dto.UpdatePersonalScheduleRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PersonalScheduleCommandMapper {

    // 개인 일정 작성
    int insertPersonalSchedule(@Param("employeeId") Long employeeId, @Param("request") CreatePersonalScheduleRequest request);

    // 개인 일정 수정
    int updatePersonalSchedule(@Param("psId") Long psId, @Param("request") UpdatePersonalScheduleRequest request);

    // 개인 일정 삭제
    int deletePersonalSchedule(@Param("psId") Long psId);
}
