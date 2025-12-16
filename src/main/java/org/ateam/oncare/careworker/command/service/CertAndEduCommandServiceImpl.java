package org.ateam.oncare.careworker.command.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.careworker.command.domain.CertAndEduStatus; // ★ Enum Import
import org.ateam.oncare.careworker.command.dto.AddCertificateDTO;
import org.ateam.oncare.careworker.command.dto.AddEducationDTO;
import org.ateam.oncare.careworker.command.dto.CertificateStatusUpdateDTO; // ★ DTO Import
import org.ateam.oncare.careworker.command.entity.*;
import org.ateam.oncare.careworker.command.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CertAndEduCommandServiceImpl implements CertAndEduCommandService {

    private final CareWorkerInfoRepository careWorkerRepository;
    private final CareWorkerCertificateRepository certificateRepository;
    private final EducationRepository educationRepository;
    private final EntityManager entityManager;

    @Override
    public void addCertificate(Long employeeId, AddCertificateDTO dto) {
        // 1. 요양보호사 조회
        CareWorker careWorker = careWorkerRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 직원은 요양보호사로 등록되어 있지 않습니다."));

        // 2. 자격증 마스터 조회
        Certificate masterCert = entityManager.getReference(Certificate.class, dto.getCertificateId());

        // 3. 자격증 저장
        CareWorkerCertificate cwc = new CareWorkerCertificate();
        cwc.setCareWorker(careWorker);
        cwc.setCertificate(masterCert);
        cwc.setLicenseNo(dto.getLicenseNo());
        cwc.setIssueDate(dto.getIssueDate());
        cwc.setExpireDate(dto.getExpireDate());

        // [변경] 하드코딩(0) 대신 Enum 사용 -> 유지보수성 향상
        cwc.setStatus(CertAndEduStatus.PENDING.getCode());

        certificateRepository.save(cwc);
    }

    @Override
    public void addEducation(Long careWorkerCertId, AddEducationDTO dto) {
        // 1. 보유 자격증 확인
        if (!certificateRepository.existsById(careWorkerCertId)) {
            throw new IllegalArgumentException("존재하지 않는 보유 자격증 ID입니다.");
        }

        // 2. 교육 이력 저장
        Education education = Education.builder()
                .careWorkerCertificateId(careWorkerCertId)
                .eduName(dto.getEduName())
                .institution(dto.getInstitution())
                .eduDate(dto.getEduDate())
                .nextEduDate(dto.getNextEduDate())
                .isOverdue(dto.getIsOverdue())
                .status(dto.getStatus() != null ? dto.getStatus() : 0)
                .build();

        educationRepository.save(education);
    }

    // [추가] 자격증 상태 변경 (승인/반려) 구현
    @Override
    public void updateCertificateStatus(Long certId, CertificateStatusUpdateDTO dto) {
        // 1. 자격증 조회
        CareWorkerCertificate certificate = certificateRepository.findById(certId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 자격증 ID입니다."));

        // 2. 문자열 상태("APPROVED") -> 숫자 상태(2) 변환 (Enum 사용)
        int statusCode = CertAndEduStatus.toCode(dto.getStatus());

        // 3. 상태 업데이트 (Dirty Checking으로 자동 DB 반영)
        certificate.setStatus(statusCode);
    }
}