package org.ateam.oncare.employee.query.service;

import org.ateam.oncare.employee.query.dto.EmployeeDetailDTO;
import org.ateam.oncare.employee.query.dto.EmployeeListDTO;
import org.ateam.oncare.employee.query.dto.EmployeeSearchCondition;

import java.util.List;

public interface EmployeeQueryService {

    /**
     * 직원 목록 조회 (검색 조건 포함)
     * 
     * @param condition 검색 조건 (키워드, 직군, 상태)
     * @return 직원 목록 리스트
     */
    List<EmployeeListDTO> getEmployeeList(EmployeeSearchCondition condition);

    /**
     * 직원 상세 조회 (기본 정보 + 경력 정보 포함)
     * 
     * @param id 직원 ID
     * @return 직원 상세 정보 DTO
     */
    EmployeeDetailDTO getEmployeeDetail(Long id);

    /**
     * 직원 방문 요양 일정 조회
     *
     * @param employeeId 직원 ID
     * @return 통합 일정 리스트 (Type: 방문요양)
     */
    List<org.ateam.oncare.employee.query.dto.ScheduleResDTO> getVisitSchedules(Integer employeeId);

    /**
     * 직원 물품(렌탈/회수) 일정 조회
     *
     * @param employeeId 직원 ID
     * @return 통합 일정 리스트 (Type: 물품수령/회수)
     */
    List<org.ateam.oncare.employee.query.dto.ScheduleResDTO> getProductSchedules(Integer employeeId);

    /**
     * 교육 예정일 임박/초과 직원 조회
     *
     * @return 교육 알림 DTO 리스트
     */
    List<org.ateam.oncare.employee.query.dto.EducationAlertDTO> getEducationAlerts();

    /**
     * 직원 검색 필터 옵션 조회 (직군, 자격증)
     *
     * @return 필터 옵션 DTO
     */
    org.ateam.oncare.employee.query.dto.EmployeeFilterResponseDTO getEmployeeFilters();

    /**
     * 현재 재직 중인 직원 수 조회
     *
     * @return 직원 수
     */
    int countActiveEmployees();

    /**
     * 현재 휴직 중인 직원 수 조회
     *
     * @return 직원 수
     */
    int countOnLeaveEmployees();

    /**
     * 직급별 직원 수 조회
     *
     * @return 직급별 직원 수 리스트
     */
    List<org.ateam.oncare.employee.query.dto.EmployeeCountByJobDTO> countEmployeesByJob();
}