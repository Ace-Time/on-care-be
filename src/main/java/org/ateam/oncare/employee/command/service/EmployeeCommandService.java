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
public class EmployeeCommandService {
    private final EmployeeCommandRepository employeeCommandRepository;
    private final EmployeeCareerCommandRepository employeeCareerCommandRepository;

    /**
     * 직원 등록
     */
    @Transactional
    public Long registerEmployee(EmployeeRequestDTO dto) {
        // 1. Employee 엔티티 생성 및 값 세팅 (Setter 사용)
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setPw("1234");              // 비번 디폴트 고정
        employee.setBirth(dto.getBirth());
        employee.setGender(dto.getGender());
        employee.setAdress(dto.getAdress());
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

        // 2. 직원 저장 (이 시점에 ID가 생성됨)
        Employee savedEmployee = employeeCommandRepository.save(employee);
        Long empId = savedEmployee.getId();

        // 3. 경력(Career) 저장
        // 엔티티 간 연관관계(@OneToMany)가 없으므로 별도로 저장해야 함
        if (dto.getCareers() != null && !dto.getCareers().isEmpty()) {
            List<EmployeeCareer> careerEntities = dto.getCareers().stream()
                    .map(cDto -> {
                        EmployeeCareer career = new EmployeeCareer();
                        career.setEmployeeId(empId); // ★ 생성된 직원 ID 주입
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

    // 직원 정보 수정
    @Transactional
    public void updateEmployee(Long id, EmployeeRequestDTO dto) {
        // 1. 기존 직원 조회
        Employee employee = employeeCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 직원이 없습니다. id=" + id));

        // 2. 정보 업데이트 (Setter로 덮어쓰기)
        employee.setName(dto.getName());
        // pw는 수정 시 건드리지 않음 (필요하면 별도 메서드로 분리)
        employee.setBirth(dto.getBirth());
        employee.setGender(dto.getGender());
        employee.setAdress(dto.getAdress());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setEmergencyNumber(dto.getEmergencyNumber());
        employee.setHireDate(dto.getHireDate());
        employee.setEndDate(dto.getEndDate());

        employee.setDeptCode(dto.getDeptCode());
        employee.setJobCode(dto.getJobCode());
        employee.setManagerId(dto.getManagerId());
        employee.setStatusId(dto.getStatusId());

        // Employee는 Dirty Checking으로 자동 저장됨.

        // 3. 경력(Career) 업데이트 전략: "기존 것 삭제 -> 새 리스트 등록"
        // 연관관계 매핑이 없어서 orphanRemoval 기능을 못 쓰므로 직접 구현
        employeeCareerCommandRepository.deleteAllByEmployeeId(id);

        if (dto.getCareers() != null && !dto.getCareers().isEmpty()) {
            List<EmployeeCareer> newCareers = dto.getCareers().stream()
                    .map(cDto -> {
                        EmployeeCareer career = new EmployeeCareer();
                        career.setEmployeeId(id); // 기존 직원 ID 유지
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
