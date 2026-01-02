package org.ateam.oncare.schedule.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.command.dto.ConfirmedVisitScheduleTimeUpdateRequest;
import org.ateam.oncare.schedule.command.dto.ConfirmedVisitScheduleTimeUpdateResponse;
import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.ateam.oncare.schedule.command.repository.VisitScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmedVisitScheduleCommandService {

    private final VisitScheduleRepository visitScheduleRepository;

    @Transactional
    public ConfirmedVisitScheduleTimeUpdateResponse updateTime(
            Long vsId,
            ConfirmedVisitScheduleTimeUpdateRequest request
    ) {
        if (vsId == null) throw new IllegalArgumentException("vsId is required");
        if (request == null) throw new IllegalArgumentException("request is required");

        LocalDateTime startDt = request.getStartDt();
        LocalDateTime endDt = request.getEndDt();

        if (startDt == null || endDt == null) {
            throw new IllegalArgumentException("startDt/endDt are required");
        }
        if (!endDt.isAfter(startDt)) {
            throw new IllegalArgumentException("endDt must be after startDt");
        }

        // ✅ 오늘 기준 과거(전날 포함) 날짜로는 수정 불가
        LocalDate today = LocalDate.now();
        if (startDt.toLocalDate().isBefore(today)) {
            throw new IllegalStateException("일정 수정은 현재 시각 이후로 가능합니다.");
        }

        VisitSchedule vs = visitScheduleRepository.findById(vsId)
                .orElseThrow(() ->
                        new IllegalArgumentException("visit_schedule not found: vsId=" + vsId)
                );

        String status = vs.getVisitStatus() == null ? "" : vs.getVisitStatus().name();
        if ("IN_PROGRESS".equals(status) || "DONE".equals(status)) {
            throw new IllegalStateException("진행/완료 일정은 시간 변경이 불가합니다.");
        }

        Long careWorkerId = vs.getCareWorkerId();
        Long beneficiaryId = vs.getBeneficiaryId();

        if (careWorkerId != null) {
            boolean overlappedCareWorker =
                    visitScheduleRepository.existsOverlapForCareWorker(
                            careWorkerId,
                            vsId,
                            startDt,
                            endDt
                    );
            if (overlappedCareWorker) {
                throw new IllegalStateException(
                        "해당 시간에 요양보호사가 이미 배치되어 있어 수정할 수 없습니다."
                );
            }
        }

        if (beneficiaryId != null) {
            boolean overlappedBeneficiary =
                    visitScheduleRepository.existsOverlapForBeneficiary(
                            beneficiaryId,
                            vsId,
                            startDt,
                            endDt
                    );
            if (overlappedBeneficiary) {
                throw new IllegalStateException(
                        "해당 시간에 수급자가 이미 배치되어 있어 수정할 수 없습니다."
                );
            }
        }

        vs.setStartDt(startDt);
        vs.setEndDt(endDt);

        VisitSchedule saved = visitScheduleRepository.save(vs);

        return ConfirmedVisitScheduleTimeUpdateResponse.builder()
                .vsId(saved.getVsId())
                .startDt(saved.getStartDt())
                .endDt(saved.getEndDt())
                .build();
    }

    @Transactional
    public void delete(Long vsId) {
        if (vsId == null) throw new IllegalArgumentException("vsId is required");

        VisitSchedule vs = visitScheduleRepository.findById(vsId)
                .orElseThrow(() ->
                        new IllegalArgumentException("visit_schedule not found: vsId=" + vsId)
                );

        String status = vs.getVisitStatus() == null ? "" : vs.getVisitStatus().name();
        if ("IN_PROGRESS".equals(status) || "DONE".equals(status)) {
            throw new IllegalStateException("진행/완료 일정은 삭제가 불가합니다.");
        }

        visitScheduleRepository.delete(vs);
    }
}