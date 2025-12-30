package org.ateam.oncare.employee.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.command.domain.CertAndEduStatus;
import org.ateam.oncare.employee.query.dto.CertificateViewDTO;
import org.ateam.oncare.employee.query.dto.EducationDTO;
import org.ateam.oncare.employee.query.service.CertAndEduQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/care-workers")
@RequiredArgsConstructor
public class CertAndEduQueryController {

    private final CertAndEduQueryService CertAndEduQueryService;

    // 1. 자격증 목록 조회
    @GetMapping("/employees/{employeeId}/certificates")
    public ResponseEntity<List<CertificateViewDTO>> getCertificates(@PathVariable Long employeeId) {
        return ResponseEntity.ok(CertAndEduQueryService.getCertificatesByEmployeeId(employeeId));
    }

    // 2. 교육 이력 조회
    @GetMapping("/certificates/{careWorkerCertId}/educations")
    public ResponseEntity<List<EducationDTO>> getEducations(@PathVariable Long careWorkerCertId) {
        return ResponseEntity.ok(CertAndEduQueryService.getEducationsByCertId(careWorkerCertId));
    }

    @GetMapping("/certificates")
    public ResponseEntity<?> getCertificates(
            @RequestParam(required = false) String status) { // "PENDING" 받음
        try {
            // 1. [번역] "PENDING" -> 0 (숫자로 변환)
            Integer statusCode = null;
            if (status != null && !status.isBlank()) {
                statusCode = CertAndEduStatus.toCode(status);
            }

            // 2. 숫자를 넘겨서 조회
            return ResponseEntity.ok(CertAndEduQueryService.getCertificates(statusCode));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage() + "\n" + e.toString());
        }
    }

    // 4. 특정 자격증(Master) 보유자 목록 조회
    // URL: GET /api/care-workers/certificates/holders/{certificateId}
    @GetMapping("/certificates/holders/{certificateId}")
    public ResponseEntity<List<CertificateViewDTO>> getCertificateHolders(@PathVariable Long certificateId) {
        return ResponseEntity.ok(CertAndEduQueryService.getCertificatesByMasterId(certificateId));
    }

    // 5. 전체 자격증 종류(Master) 조회
    // URL: GET /api/care-workers/certificates/types
    @GetMapping("/certificates/types")
    public ResponseEntity<List<org.ateam.oncare.employee.query.dto.CertificateMasterDTO>> getAllCertificateTypes() {
        return ResponseEntity.ok(CertAndEduQueryService.getAllCertificates());
    }
}