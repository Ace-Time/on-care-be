package org.ateam.oncare.employee.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.employee.query.dto.CertificateViewDTO;
import org.ateam.oncare.employee.query.dto.EducationDTO;

import java.util.List;

@Mapper
public interface CertAndEduQueryMapper {

    // 1. 특정 직원의 자격증 목록 조회
    List<CertificateViewDTO> selectCertificatesByEmployeeId(Long employeeId);

    // ★ [수정/추가] 상태 코드로 조회
    List<CertificateViewDTO> selectCertificates(@org.apache.ibatis.annotations.Param("statusCode") Integer statusCode);

    // ★ [추가] 특정 자격증(Master)을 보유한 직원 목록 조회
    List<CertificateViewDTO> selectCertificatesByMasterId(Long masterId);

    // ★ [추가] 전체 자격증 종류(Master) 조회
    List<org.ateam.oncare.employee.query.dto.CertificateMasterDTO> selectAllCertificates();

    // 2. 특정 보유 자격증의 교육 이력 조회
    List<EducationDTO> selectEducationsByCertId(Long careWorkerCertId);

    // 보수교육 알림 대상 조회 (기준일 이하의 nextEduDate)
    List<org.ateam.oncare.employee.query.dto.EducationAlertDTO> selectEducationAlerts(
            @org.apache.ibatis.annotations.Param("thresholdDate") java.time.LocalDate thresholdDate);
}