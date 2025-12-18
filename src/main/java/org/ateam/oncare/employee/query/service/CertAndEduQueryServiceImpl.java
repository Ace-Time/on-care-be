package org.ateam.oncare.employee.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.query.dto.CertificateViewDTO;
import org.ateam.oncare.employee.query.dto.EducationDTO;
import org.ateam.oncare.employee.query.mapper.CertAndEduQueryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // MyBatis 조회 성능 최적화
public class CertAndEduQueryServiceImpl implements CertAndEduQueryService {

    private final CertAndEduQueryMapper certAndEduQueryMapper;

    @Override
    public List<CertificateViewDTO> getCertificatesByEmployeeId(Long employeeId) {
        return certAndEduQueryMapper.selectCertificatesByEmployeeId(employeeId);
    }

    @Override
    public List<EducationDTO> getEducationsByCertId(Long careWorkerCertId) {
        return certAndEduQueryMapper.selectEducationsByCertId(careWorkerCertId);
    }

    // ★ 인터페이스에 정의한 것과 똑같은 파라미터(Integer)를 받아야 합니다.
    @Override
    public List<CertificateViewDTO> getCertificates(Integer statusCode) {
        return certAndEduQueryMapper.selectCertificates(statusCode);
    }
}