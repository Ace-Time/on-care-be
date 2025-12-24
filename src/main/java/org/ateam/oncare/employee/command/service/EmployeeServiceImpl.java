package org.ateam.oncare.employee.command.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    // 요양보호사 직종 코드 (DB의 m_job 테이블 id와 맞춰주세요. 예: 1)
    private static final Long JOB_CODE_CARE_WORKER = 1L;

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
    @Transactional // ★ 필수: 도중에 에러나면 전체 취소되어야 함
    public void registerEmployee(EmployeeRegisterDto dto) {
        // 1. 직원(Employee) 테이블에 등록
        employeeMapper.insertEmployee(dto);
        // MyBatis의 selectKey/useGeneratedKeys 덕분에 dto.getId()에 새 ID가 들어있음

        // 2. 만약 직종이 '요양보호사'라면, CareWorker 테이블에도 등록
        if (JOB_CODE_CARE_WORKER.equals(dto.getJobCode())) {
            employeeMapper.insertCareWorker(dto.getId());
        }
    }
}
