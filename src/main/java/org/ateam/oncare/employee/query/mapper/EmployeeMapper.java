package org.ateam.oncare.employee.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.employee.query.dto.*;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    // 직원 목록 조회 (검색 조건 포함)
    List<EmployeeListDTO> selectEmployeeList(EmployeeSearchCondition condition);

    // 직원 상세 기본 정보 조회
    EmployeeDetailDTO selectEmployeeDetail(Long id);

    // 직원 경력 리스트 조회
    List<EmployeeDetailDTO.CareerDTO> selectEmployeeCareers(Long employeeId);

    // 자격증 조회
    List<CertificateViewDTO> selectEmployeeCertificates(Long employeeId);

    // 서비스 유형 조회
    List<ServiceTypeDTO> selectServiceTypes(Long employeeId);

    // 교육 이력 조회
    List<EducationDTO> selectEducations(Long employeeId);
}