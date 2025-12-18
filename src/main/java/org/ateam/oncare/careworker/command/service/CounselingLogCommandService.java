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

    @Transactional
    public void createCounselingLog(Long careWorkerId, CreateCounselingLogRequest request) {
        log.info("방문상담 작성 시작 - beneficiaryId: {}", request.getBeneficiaryId());
        int inserted = counselingLogCommandMapper.insertCounselingLog(careWorkerId, request);

        if (inserted == 0) {
            throw new IllegalStateException("방문상담 작성에 실패했습니다.");
        }

        log.info("방문상담 작성 완료");
    }

    @Transactional
    public void updateCounselingLog(Long counselingId, UpdateCounselingLogRequest request) {
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
