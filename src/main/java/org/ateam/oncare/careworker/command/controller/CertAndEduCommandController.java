package org.ateam.oncare.careworker.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.careworker.command.dto.AddCertificateDTO;
import org.ateam.oncare.careworker.command.dto.AddEducationDTO;
import org.ateam.oncare.careworker.command.service.CertAndEduCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/care-workers")
@RequiredArgsConstructor
public class CertAndEduCommandController {

    private final CertAndEduCommandService certAndEduCommandService;

    /**
     * 1. 특정 직원에게 [자격증]만 추가
     * URL: POST /api/care-workers/employees/{employeeId}/certificates
     */
    @PostMapping("/employees/{employeeId}/certificates")
    public ResponseEntity<String> addCertificate(
            @PathVariable Long employeeId,
            @RequestBody AddCertificateDTO dto) {

        certAndEduCommandService.addCertificate(employeeId, dto);
        return ResponseEntity.ok("자격증이 추가되었습니다.");
    }

    /**
     * 2. 보유 중인 특정 자격증에 [보수 교육]만 추가
     * URL: POST /api/care-workers/certificates/{careWorkerCertId}/educations
     * 주의: certificateId(마스터ID)가 아니라 보유 자격증의 PK(ID)입니다.
     */
    @PostMapping("/certificates/{careWorkerCertId}/educations")
    public ResponseEntity<String> addEducation(
            @PathVariable Long careWorkerCertId,
            @RequestBody AddEducationDTO dto) {

        certAndEduCommandService.addEducation(careWorkerCertId, dto);
        return ResponseEntity.ok("보수 교육 이력이 추가되었습니다.");
    }

    @PatchMapping("/certificates/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long id,
            @RequestBody org.ateam.oncare.careworker.command.dto.CertificateStatusUpdateDTO dto) {

        certAndEduCommandService.updateCertificateStatus(id, dto);
        return ResponseEntity.ok("상태가 변경되었습니다.");
    }
}