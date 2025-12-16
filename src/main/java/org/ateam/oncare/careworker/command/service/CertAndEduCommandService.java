package org.ateam.oncare.careworker.command.service;

import org.ateam.oncare.careworker.command.dto.AddCertificateDTO;
import org.ateam.oncare.careworker.command.dto.AddEducationDTO;
import org.ateam.oncare.careworker.command.dto.CertificateStatusUpdateDTO;

public interface CertAndEduCommandService {

    /**
     * 특정 직원에게 자격증 추가
     * @param employeeId 직원 ID
     * @param dto 자격증 정보 (CertAndEdu...)
     */
    void addCertificate(Long employeeId, AddCertificateDTO dto);

    /**
     * 특정 자격증에 보수 교육 이력 추가
     * @param careWorkerCertId 보유 자격증 ID (PK)
     * @param dto 교육 이력 정보 (CertAndEdu...)
     */
    void addEducation(Long careWorkerCertId, AddEducationDTO dto);

    void updateCertificateStatus(Long certId, CertificateStatusUpdateDTO dto);
}