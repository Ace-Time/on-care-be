package org.ateam.oncare.employee.command.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.careworker.command.entity.*;
import org.ateam.oncare.careworker.command.repository.*;
import org.ateam.oncare.employee.command.dto.*;
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
@Transactional
public class EmployeeCommandServiceImpl implements EmployeeCommandService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final EmployeeCareerCommandRepository employeeCareerCommandRepository;

    // 요양보호사 관련 리포지토리
    private final CareWorkerInfoRepository careWorkerRepository;
    private final CareWorkerCertificateRepository certificateRepository;
    private final CareWorkerServiceTypeRepository serviceTypeRepository;
    private final EducationRepository educationRepository;

    private final EntityManager entityManager;

    @Override
    public Long registerEmployee(EmployeeRequestDTO dto) {
        // 1. [Employee] 직원 엔티티 생성 및 저장
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setPw("1234");
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
        employee.setStatusId(dto.getStatusId());
        employee.setManagerId(dto.getManagerId());

        Employee savedEmployee = employeeCommandRepository.save(employee);
        Long empId = savedEmployee.getId();

        // 2. [CareWorker] 요양보호사 테이블 정보 생성
        CareWorker careWorker = CareWorker.builder()
                .employeeId(empId)
                .build();
        CareWorker savedCareWorker = careWorkerRepository.save(careWorker);

        // 3. [Career] 경력 정보 저장
        saveCareers(empId, dto.getCareers());

        // 4. [ServiceType] 서비스 유형 저장 (★ 추가됨)
        saveServiceTypes(savedCareWorker.getId(), dto.getServiceTypeIds());

        // 5. [Certificate & Education] 자격증 및 교육 이력 저장 (★ 수정됨)
        saveCertificatesAndEducations(savedCareWorker, dto.getCertificates());

        return empId;
    }

    @Override
    public void updateEmployee(Long id, EmployeeRequestDTO dto) {
        // 1. 직원 조회 및 수정
        Employee employee = employeeCommandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다. ID=" + id));

        employee.setName(dto.getName());
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
        employee.setStatusId(dto.getStatusId());
        employee.setManagerId(dto.getManagerId());

        // 2. [CareWorker] 요양보호사 정보 조회
        CareWorker careWorker = careWorkerRepository.findByEmployeeId(id)
                .orElseGet(() -> {
                    CareWorker newWorker = CareWorker.builder().employeeId(id).build();
                    return careWorkerRepository.save(newWorker);
                });

        // 3. 경력 정보 갱신
        employeeCareerCommandRepository.deleteAllByEmployeeId(id);
        saveCareers(id, dto.getCareers());

        // 4. [ServiceType] 서비스 유형 갱신 (삭제 후 재등록) (★ 추가됨)
        serviceTypeRepository.deleteAllByCareWorkerId(careWorker.getId());
        saveServiceTypes(careWorker.getId(), dto.getServiceTypeIds());

        // 5. [Certificate & Education] 자격증 및 교육 이력 갱신 (★ 수정됨)
        // FK 제약조건 때문에 [교육 삭제 -> 자격증 삭제 -> 새 자격증 등록 -> 새 교육 등록] 순서 필수

        // (1) 기존 자격증 조회
        List<CareWorkerCertificate> oldCerts = certificateRepository.findAllByCareWorkerId(careWorker.getId());

        // (2) 기존 자격증에 딸린 교육 이력 먼저 삭제
        if (!oldCerts.isEmpty()) {
            List<Long> oldCertIds = oldCerts.stream()
                    .map(CareWorkerCertificate::getId)
                    .collect(Collectors.toList());

            // EducationRepository에 해당 메서드가 없다면 반복문으로 삭제하거나, @Query로 구현된 메서드 사용
            educationRepository.deleteAllByCareWorkerCertificateIdIn(oldCertIds);
        }

        // (3) 자격증 삭제
        certificateRepository.deleteAllByCareWorkerId(careWorker.getId());

        // (4) 새 데이터 등록
        saveCertificatesAndEducations(careWorker, dto.getCertificates());
    }

    // --- Helper Methods ---

    private void saveCareers(Long empId, List<CareerDTO> careerDtos) {
        if (careerDtos != null && !careerDtos.isEmpty()) {
            List<EmployeeCareer> careers = careerDtos.stream()
                    .map(cDto -> {
                        EmployeeCareer career = new EmployeeCareer();
                        career.setEmployeeId(empId);
                        career.setCompanyName(cDto.getCompanyName());
                        career.setWorkPeriod(cDto.getWorkPeriod());
                        career.setTask(cDto.getTask());
                        return career;
                    })
                    .collect(Collectors.toList());
            employeeCareerCommandRepository.saveAll(careers);
        }
    }

    // ★ 서비스 유형 저장 로직
    private void saveServiceTypes(Long careWorkerId, List<Long> serviceTypeIds) {
        if (serviceTypeIds != null && !serviceTypeIds.isEmpty()) {
            List<CareWorkerServiceType> list = serviceTypeIds.stream()
                    .map(typeId -> new CareWorkerServiceType(typeId, careWorkerId)) // 엔티티 생성자 사용
                    .collect(Collectors.toList());
            serviceTypeRepository.saveAll(list);
        }
    }

    // ★ 자격증 및 교육 이력 저장 로직 (중첩 저장)
    private void saveCertificatesAndEducations(CareWorker careWorker, List<CertificateInputDTO> dtos) {
        if (dtos != null && !dtos.isEmpty()) {
            for (CertificateInputDTO dto : dtos) {
                // 안전장치: 자격증 ID가 없으면 스킵
                if (dto.getCertificateId() == null) continue;

                // 1. 자격증 마스터 조회
                Certificate masterCert = entityManager.getReference(Certificate.class, dto.getCertificateId());

                // 2. 자격증 엔티티 생성 (Setter 방식)
                CareWorkerCertificate cwc = new CareWorkerCertificate();
                cwc.setCareWorker(careWorker);
                cwc.setCertificate(masterCert);
                cwc.setLicenseNo(dto.getLicenseNo());
                cwc.setIssueDate(dto.getIssueDate());
                cwc.setExpireDate(dto.getExpireDate());
                cwc.setStatus(0);

                // 3. 자격증 먼저 저장 (그래야 ID가 생성됨)
                CareWorkerCertificate savedCwc = certificateRepository.save(cwc);

                // 4. 생성된 자격증 ID를 사용하여 교육 이력 저장
                saveEducations(savedCwc.getId(), dto.getEducations());
            }
        }
    }

    // ★ 교육 이력 저장 로직
    private void saveEducations(Long certId, List<EducationInputDTO> eduDtos) {
        if (eduDtos != null && !eduDtos.isEmpty()) {
            List<Education> eduList = eduDtos.stream()
                    .map(eDto -> Education.builder()
                            .careWorkerCertificateId(certId) // 상위 자격증 ID 연결
                            .eduName(eDto.getEduName())
                            .institution(eDto.getInstitution())
                            .eduDate(eDto.getEduDate())
                            .nextEduDate(eDto.getNextEduDate())
                            .isOverdue(eDto.getIsOverdue())
                            .status(eDto.getStatus() != null ? eDto.getStatus() : 0)
                            .build())
                    .collect(Collectors.toList());
            educationRepository.saveAll(eduList);
        }
    }
}