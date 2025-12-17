package org.ateam.oncare.employee.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.command.dto.EmployeeRequestDTO;
import org.ateam.oncare.employee.command.entity.Employee;
import org.ateam.oncare.employee.command.entity.EmployeeCareer;
import org.ateam.oncare.employee.command.repository.EmployeeCareerCommandRepository;
import org.ateam.oncare.employee.command.repository.EmployeeCommandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional // 클래스 레벨에 붙이면 모든 메서드에 트랜잭션 적용
public class EmployeeCommandServiceImpl implements EmployeeCommandService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final EmployeeCareerCommandRepository employeeCareerCommandRepository;

    @Override
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

        // 2. 직원 저장
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
    public void updateEmployee(Integer id, EmployeeRequestDTO dto) {
        // 1. 기존 직원 조회
        Employee employee = employeeCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 직원이 없습니다. id=" + id));

        // 2. 정보 업데이트 (Setter로 덮어쓰기 -> Dirty Checking)
        employee.setName(dto.getName());
        // pw는 유지
        employee.setBirth(dto.getBirth());
        employee.setGender(dto.getGender());
        employee.setAddress(dto.getAddress());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setEmergencyNumber(dto.getEmergencyNumber());
        employee.setHireDate(dto.getHireDate());
        employee.setEndDate(dto.getEndDate());

        employee.setDeptCode(dto.getDeptCode());
        employee.setJobCode(dto.getJobCode());
        employee.setManagerId(dto.getManagerId());
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