package org.ateam.oncare.employee.query.service;

import org.ateam.oncare.employee.query.dto.CertificateViewDTO;
import org.ateam.oncare.employee.query.dto.EducationDTO;

import java.util.List;

public interface CertAndEduQueryService {
    // 직원별 자격증 목록 조회
    List<CertificateViewDTO> getCertificatesByEmployeeId(Long employeeId);

    // 자격증별 교육 이력 목록 조회
    List<EducationDTO> getEducationsByCertId(Long careWorkerCertId);

    /**
     * 자격증 목록 조회 (필터링 포함)
     * 
     * @param statusCode 상태 코드 (null이면 전체 조회, 0:대기, 1:반려, 2:승인)
     */
    // ★ 이 줄을 반드시 추가해야 합니다!
    List<CertificateViewDTO> getCertificates(Integer statusCode);

    // 자격증(Master) ID로 해당 자격증 보유자 목록 조회
    List<CertificateViewDTO> getCertificatesByMasterId(Long masterId);

    // 전체 자격증(Master) 목록 조회
    List<org.ateam.oncare.employee.query.dto.CertificateMasterDTO> getAllCertificates();
}