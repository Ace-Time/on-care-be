package org.ateam.oncare.careworker.command.service;

import org.ateam.oncare.careworker.command.dto.CreateCounselingLogRequest;
import org.ateam.oncare.careworker.command.dto.UpdateCounselingLogRequest;
import org.ateam.oncare.careworker.command.mapper.CounselingLogCommandMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CounselingLogCommandService {

    private final CounselingLogCommandMapper counselingLogCommandMapper;

    private String saveSignature(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }

        try {
            // Remove header if present (e.g., "data:image/png;base64,")
            String base64Data = base64Image;
            if (base64Image.contains(",")) {
                base64Data = base64Image.split(",")[1];
            }

            byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Data);
            String fileName = java.util.UUID.randomUUID().toString() + ".png";
            String uploadDir = "uploads/signatures/";
            java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);

            if (!java.nio.file.Files.exists(uploadPath)) {
                java.nio.file.Files.createDirectories(uploadPath);
            }

            java.nio.file.Path filePath = uploadPath.resolve(fileName);
            java.nio.file.Files.write(filePath, decodedBytes);

            return "/" + uploadDir + fileName; // Return relative path for DB
        } catch (java.io.IOException e) {
            log.error("서명 이미지 저장 중 오류 발생", e);
            throw new RuntimeException("서명 이미지 저장 실패", e);
        }
    }

    @Transactional
    public void createCounselingLog(Long employeeId, CreateCounselingLogRequest request) {
        // 서명 이미지 저장 처리
        if (request.getCounselorSignImage() != null) {
            String path = saveSignature(request.getCounselorSignImage());
            if (path != null)
                request.setCounselorSignUrl(path);
        }
        if (request.getGuardianSignImage() != null) {
            String path = saveSignature(request.getGuardianSignImage());
            if (path != null)
                request.setGuardianSignUrl(path);
        }

        log.info("방문상담 작성 시작 - employeeId: {}, beneficiaryId: {}", employeeId, request.getBeneficiaryId());
        int inserted = counselingLogCommandMapper.insertCounselingLog(employeeId, request);

        if (inserted == 0) {
            throw new IllegalStateException("방문상담 작성에 실패했습니다.");
        }

        log.info("방문상담 작성 완료");
    }

    @Transactional
    public void updateCounselingLog(Long counselingId, UpdateCounselingLogRequest request) {
        // 서명 이미지 저장 처리
        if (request.getCounselorSignImage() != null) {
            String path = saveSignature(request.getCounselorSignImage());
            if (path != null)
                request.setCounselorSignUrl(path);
        }
        if (request.getGuardianSignImage() != null) {
            String path = saveSignature(request.getGuardianSignImage());
            if (path != null)
                request.setGuardianSignUrl(path);
        }

        log.info("방문상담 수정 시작 - counselingId: {}", counselingId);
        int updated = counselingLogCommandMapper.updateCounselingLog(counselingId, request);

        if (updated == 0) {
            throw new IllegalArgumentException("해당 방문상담을 찾을 수 없습니다. counselingId: " + counselingId);
        }

        log.info("방문상담 수정 완료 - counselingId: {}", counselingId);
    }

    @Transactional
    public void deleteCounselingLog(Long counselingId) {
        log.info("방문상담 삭제 시작 - counselingId: {}", counselingId);
        int deleted = counselingLogCommandMapper.deleteCounselingLog(counselingId);

        if (deleted == 0) {
            throw new IllegalArgumentException("해당 방문상담을 찾을 수 없습니다. counselingId: " + counselingId);
        }

        log.info("방문상담 삭제 완료 - counselingId: {}", counselingId);
    }
}
