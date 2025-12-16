package org.ateam.oncare.auth.command.mapper;

import org.ateam.oncare.auth.command.dto.ResponseLoginEmployeeDTO;
import org.mapstruct.Mapper;

// componentModel = "spring" -> 자동으로 스프링 빈(@Component)으로 등록
@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    ResponseLoginEmployeeDTO toLoginDTO(org.ateam.oncare.employee.command.dto.ResponseLoginEmployeeDTO source);
}
