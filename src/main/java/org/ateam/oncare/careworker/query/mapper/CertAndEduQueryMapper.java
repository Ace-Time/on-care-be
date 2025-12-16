package org.ateam.oncare.careworker.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.employee.query.dto.CertificateViewDTO;
import org.ateam.oncare.employee.query.dto.EducationDTO;

import java.util.List;

@Mapper
public interface CertAndEduQueryMapper {

    // 1. 특정 직원의 자격증 목록 조회
    List<CertificateViewDTO> selectCertificatesByEmployeeId(Long employeeId);

    // ★ [수정/추가] 상태 코드로 조회하는 메서드 선언이 필요합니다!
    List<CertificateViewDTO> selectCertificates(Integer statusCode);

    // 2. 특정 보유 자격증의 교육 이력 조회
    List<EducationDTO> selectEducationsByCertId(Long careWorkerCertId);
}