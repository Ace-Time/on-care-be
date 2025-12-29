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

    // 요양보호사(담당수급자) 일정 조회
    List<VisitScheduleVO> selectSchedulesByEmployeeId(Integer employeeId);

    // 물품 회수/수령 일정 조회
    List<ProductTaskVO> selectProductTasksByEmployeeId(Long employeeId);

    // 교육 예정일 임박/초과 직원 조회
    List<EducationAlertDTO> selectEducationAlerts();

    // 필터 옵션 조회
    List<org.ateam.oncare.employee.query.dto.FilterOptionDTO> selectJobFilters();

    List<org.ateam.oncare.employee.query.dto.FilterOptionDTO> selectCertificateFilters();

    List<org.ateam.oncare.employee.query.dto.FilterOptionDTO> selectServiceTypeFilters();

    // 재직 중인 직원 수 조회
    int countActiveEmployees();

    // 휴직 중인 직원 수 조회
    int countOnLeaveEmployees();

    // 방문 일정 조회 (상세)
    List<VisitScheduleVO> selectVisitSchedules(java.util.Map<String, Object> params);

    // 담당 수급자 조회 (요양보호사 ID 기준)
    List<org.ateam.oncare.employee.command.dto.AssignedBeneficiaryDTO> selectAssignedBeneficiaries(Long careWorkerId);

    void insertEmployee(org.ateam.oncare.employee.command.dto.EmployeeRegisterDto dto);

    void insertCareWorker(@org.apache.ibatis.annotations.Param("employeeId") Long employeeId);

    // 직급별 직원 수 조회
    List<EmployeeCountByJobDTO> countEmployeesByJob();
}