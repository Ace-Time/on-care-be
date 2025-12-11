package org.ateam.oncare.employee.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.employee.query.dto.EmployeeDetailDTO;
import org.ateam.oncare.employee.query.dto.EmployeeListDTO;
import org.ateam.oncare.employee.query.dto.EmployeeSearchCondition;
import java.util.List;

@Mapper
public interface EmployeeMapper {

    // 1. 직원 목록 조회 (검색 조건 포함)
    List<EmployeeListDTO> selectEmployeeList(EmployeeSearchCondition condition);

    // 2. 직원 상세 기본 정보 조회
    EmployeeDetailDTO selectEmployeeDetail(Long id);

    // 3. 직원 경력 리스트 조회
    List<EmployeeDetailDTO.CareerDTO> selectEmployeeCareers(Long employeeId);
}