package org.ateam.oncare.employee.command.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.alarm.command.dto.NotificationRequest;
import org.ateam.oncare.alarm.command.service.NotificationCommandService;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.employee.command.dto.RequestAuthorityDTO;
import org.ateam.oncare.employee.command.dto.EmployeeRegisterDto; // Added
import org.ateam.oncare.employee.command.dto.ResponseAuthorityDTO;
import org.ateam.oncare.employee.command.dto.ResponseLoginEmployeeDTO;
import org.ateam.oncare.employee.command.entity.Authority;
import org.ateam.oncare.employee.command.entity.Employee;
import org.ateam.oncare.employee.command.repository.AuthorityRepository;
import org.ateam.oncare.employee.command.repository.EmployeeRepository;
import org.ateam.oncare.employee.query.mapper.EmployeeMapper; // Added
import org.ateam.oncare.global.enums.MasterInternalType;
import org.ateam.oncare.global.eventType.MasterDataEvent;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import org.ateam.oncare.employee.command.dto.EmployeeRequestDTO;
import org.ateam.oncare.employee.command.entity.Employee;
import org.ateam.oncare.employee.command.entity.EmployeeCareer;
import org.ateam.oncare.employee.command.repository.EmployeeCommandRepository;
import org.ateam.oncare.employee.command.repository.EmployeeCareerCommandRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final AuthorityRepository authorityRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher applicationEventPublisher; // 변경 사항을 알리기 위함.
    private final EmployeeMapper employeeMapper;
    private final NotificationCommandService notificationCommandService; // 알림 발송 도구

    // [추가] JPA Repositories
    private final EmployeeCommandRepository employeeCommandRepository;
    private final EmployeeCareerCommandRepository employeeCareerCommandRepository;

    // 요양보호사 직종 코드 (DB의 m_job 테이블 id와 맞춰주세요. 예: 1)
    private static final Long JOB_CODE_CARE_WORKER = 5L;

    @Override
    public ResponseLoginEmployeeDTO getEmployee(RequestLogin loginRequest) {
        Employee employee = employeeRepository.findByEmail(loginRequest.getUseremail())
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));

        ResponseLoginEmployeeDTO loginEmployeeDTO = modelMapper.map(employee, ResponseLoginEmployeeDTO.class);

        return loginEmployeeDTO;
    }

    @Override
    public Map<Long, String> getAuthorityMasters() {
        List<Authority> authorities = authorityRepository.findAll();

        return authorities.stream()
                .collect(Collectors.toMap(
                        Authority::getId,
                        Authority::getName));
    }

    /**
     * 권한 마스터정보 수정시 MasterInternalType.AUTHORITY 구독 중인 모든 클라이언트에 이벤트 알림 캐싱 삭제
     * 
     * @param requestAuthorityDTO
     * @return
     */
    @Override
    @Transactional
    public ResponseAuthorityDTO updateAuthority(RequestAuthorityDTO requestAuthorityDTO) {
        Authority authority = modelMapper.map(requestAuthorityDTO, Authority.class);
        authorityRepository.save(authority);

        MasterDataEvent masterDataEvent = new MasterDataEvent(MasterInternalType.AUTHORITY);
        applicationEventPublisher.publishEvent(masterDataEvent);

        return modelMapper.map(authority, ResponseAuthorityDTO.class);
    }

    @Override
    @Transactional
    public Integer registerEmployee(EmployeeRequestDTO dto) {
        // 1. Employee 엔티티 생성 및 값 세팅
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setPw("1234"); // 비밀번호 디폴트 고정
        employee.setBirth(dto.getBirth());
        employee.setGender(dto.getGender());
        employee.setAddress(dto.getAddress());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setEmergencyNumber(dto.getEmergencyNumber());
        employee.setHireDate(dto.getHireDate());
        employee.setEndDate(dto.getEndDate());

        // FK ID 세팅
        employee.setDeptCode(dto.getDeptCode());
        employee.setJobCode(dto.getJobCode());
        employee.setManagerId(dto.getManagerId());
        employee.setStatusId(dto.getStatusId());

        // 2. 직원 저장 (JPA)
        Employee savedEmployee = employeeCommandRepository.save(employee);
        Integer empId = savedEmployee.getId();

        // 3. 경력(Career) 저장 (별도 테이블 처리)
        if (dto.getCareers() != null && !dto.getCareers().isEmpty()) {
            List<EmployeeCareer> careerEntities = dto.getCareers().stream()
                    .map(cDto -> {
                        EmployeeCareer career = new EmployeeCareer();
                        career.setEmployeeId(empId); // 생성된 직원 ID 주입
                        career.setCompanyName(cDto.getCompanyName());
                        career.setWorkPeriod(cDto.getWorkPeriod());
                        career.setTask(cDto.getTask());
                        return career;
                    })
                    .collect(Collectors.toList());

            employeeCareerCommandRepository.saveAll(careerEntities);
        }

        return empId;
    }

    @Override
    @Transactional
    public void updateEmployee(Integer id, EmployeeRequestDTO dto) {
        // 1. 기존 직원 조회
        Employee employee = employeeCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 직원이 없습니다. id=" + id));

        // 2. 정보 업데이트 (Setter로 덮어쓰기 -> Dirty Checking)
        if (dto.getName() != null)
            employee.setName(dto.getName());
        // pw는 유지
        if (dto.getBirth() != null)
            employee.setBirth(dto.getBirth());
        if (dto.getGender() != null)
            employee.setGender(dto.getGender());
        if (dto.getAddress() != null)
            employee.setAddress(dto.getAddress());
        if (dto.getEmail() != null)
            employee.setEmail(dto.getEmail());
        if (dto.getPhone() != null)
            employee.setPhone(dto.getPhone());
        if (dto.getEmergencyNumber() != null)
            employee.setEmergencyNumber(dto.getEmergencyNumber());
        if (dto.getHireDate() != null)
            employee.setHireDate(dto.getHireDate());
        if (dto.getEndDate() != null)
            employee.setEndDate(dto.getEndDate());

        if (dto.getDeptCode() != null)
            employee.setDeptCode(dto.getDeptCode());
        if (dto.getJobCode() != null)
            employee.setJobCode(dto.getJobCode());
        if (dto.getManagerId() != null)
            employee.setManagerId(dto.getManagerId());
        if (dto.getStatusId() != null)
            employee.setStatusId(dto.getStatusId());

        // 3. 경력(Career) 업데이트 전략: "전체 삭제 후 재등록"
        employeeCareerCommandRepository.deleteAllByEmployeeId(id);

        if (dto.getCareers() != null && !dto.getCareers().isEmpty()) {
            List<EmployeeCareer> newCareers = dto.getCareers().stream()
                    .map(cDto -> {
                        EmployeeCareer career = new EmployeeCareer();
                        career.setEmployeeId(id); // 기존 직원 ID 사용
                        career.setCompanyName(cDto.getCompanyName());
                        career.setWorkPeriod(cDto.getWorkPeriod());
                        career.setTask(cDto.getTask());
                        return career;
                    })
                    .collect(Collectors.toList());

            employeeCareerCommandRepository.saveAll(newCareers);
        }
    }
}
