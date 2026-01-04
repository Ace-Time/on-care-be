package org.ateam.oncare.employee.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.query.dto.*;
import org.ateam.oncare.employee.query.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회 전용 최적화
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

    private final EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeListDTO> getEmployeeList(EmployeeSearchCondition condition) {
        return employeeMapper.selectEmployeeList(condition);
    }

    @Override
    public EmployeeDetailDTO getEmployeeDetail(Long id) {
        // 1. 직원 기본 상세 정보 조회
        EmployeeDetailDTO detail = employeeMapper.selectEmployeeDetail(id);

        if (detail != null) {
            // 2. 경력 리스트 조회 및 세팅
            detail.setCareers(employeeMapper.selectEmployeeCareers(id));

            // ★ [여기가 빠져있는지 확인하세요!] ★
            // 자격증 리스트를 조회해서 DTO에 넣어줘야 합니다.
            detail.setCertificates(employeeMapper.selectEmployeeCertificates(id));

            // 서비스 유형 (방문요양, 방문목욕 등)
            detail.setServiceTypes(employeeMapper.selectServiceTypes(id));

            // 보수 교육 이력
            detail.setEducations(employeeMapper.selectEducations(id));
        }
        return detail;

    }

    @Override
    public List<ScheduleResDTO> getVisitSchedules(Integer employeeId) {
        List<VisitScheduleVO> visits = employeeMapper.selectSchedulesByEmployeeId(employeeId);
        return visits.stream()
                .map(this::convertVisitToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResDTO> getProductSchedules(Integer employeeId) {
        List<ProductTaskVO> tasks = employeeMapper.selectProductTasksByEmployeeId(Long.valueOf(employeeId));
        return tasks.stream()
                .map(this::convertTaskToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EducationAlertDTO> getEducationAlerts() {
        List<EducationAlertDTO> alerts = employeeMapper.selectEducationAlerts();

        // 상태값(OVERDUE, WARNING) 세팅
        for (EducationAlertDTO dto : alerts) {
            if (dto.getDDay() < 0) {
                dto.setStatus("OVERDUE"); // 기한 초과
            } else {
                dto.setStatus("WARNING"); // 기한 임박
            }
        }
        return alerts;
    }

    @Override
    public EmployeeFilterResponseDTO getEmployeeFilters() {
        // 1. 직군 목록 조회 (m_job)
        List<FilterOptionDTO> jobs = employeeMapper.selectJobFilters();

        // 2. 자격증 목록 조회 (certificate)
        List<FilterOptionDTO> certificates = employeeMapper.selectCertificateFilters();

        // 3. 서비스 타입 목록 조회
        List<FilterOptionDTO> serviceTypes = employeeMapper.selectServiceTypeFilters();

        return EmployeeFilterResponseDTO.builder()
                .jobs(jobs)
                .certificates(certificates)
                .serviceTypes(serviceTypes)
                .build();
    }

    @Override
    public int countActiveEmployees() {
        return employeeMapper.countActiveEmployees();
    }

    @Override
    public int countOnLeaveEmployees() {
        return employeeMapper.countOnLeaveEmployees();
    }

    @Override
    public List<EmployeeCountByJobDTO> countEmployeesByJob() {
        return employeeMapper.countEmployeesByJob();
    }

    // [기존] 방문 일정 변환 메서드
    private ScheduleResDTO convertVisitToDto(VisitScheduleVO vo) {
        String serviceName = getServiceName(vo.getServiceTypeId());
        String color = getServiceColor(vo.getServiceTypeId());
        String title = String.format("%s (%s)", vo.getBeneficiaryName(), serviceName);

        return ScheduleResDTO.builder()
                .id("visit-" + vo.getVsId())
                .title(title)
                .start(vo.getStartDt())
                .end(vo.getEndDt())
                .backgroundColor(color)
                .borderColor(color)
                .textColor("#ffffff")
                .allDay(false)
                .extendedProps(new ScheduleResDTO.ExtendedProps(serviceName, vo.getVisitStatus()))
                .build();
    }

    // [NEW] 물품 일정 변환 메서드
    private ScheduleResDTO convertTaskToDto(ProductTaskVO vo) {
        // status 0: 회수, 1: 수령
        String typeName = (vo.getStatus() == 1) ? "물품수령" : "물품회수";
        String color = (vo.getStatus() == 1) ? "#f97316" : "#ea580c"; // 주황색 계열 (수령: 밝은주황, 회수: 진한주황)

        // 제목: "홍길동 - 휠체어 (수령)"
        String title = String.format("%s - %s (%s)", vo.getBeneficiaryName(), vo.getProductName(), typeName);

        return ScheduleResDTO.builder()
                .id("task-" + vo.getId())
                .title(title)
                .start(vo.getExpectedDate().toString())
                .end(vo.getExpectedDate().toString())
                .backgroundColor(color)
                .borderColor(color)
                .textColor("#ffffff")
                .allDay(true)
                .extendedProps(new ScheduleResDTO.ExtendedProps(typeName, vo.getIsConfirmed()))
                .build();
    }

    private String getServiceName(Long typeId) {
        if (typeId == 1L)
            return "방문요양";
        if (typeId == 2L)
            return "방문목욕";
        if (typeId == 3L)
            return "방문간호";
        return "기타";
    }

    private String getServiceColor(Long typeId) {
        if (typeId == 1L)
            return "#10b981"; // 초록
        if (typeId == 2L)
            return "#3b82f6"; // 파랑
        if (typeId == 3L)
            return "#f43f5e"; // 빨강
        return "#6b7280"; // 회색
    }
}