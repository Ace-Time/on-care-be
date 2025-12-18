package org.ateam.oncare.careworker.command.service;

import org.ateam.oncare.careworker.command.dto.CompleteVisitRequest;
import org.ateam.oncare.careworker.command.dto.StartVisitRequest;
import org.ateam.oncare.careworker.command.mapper.VisitScheduleCommandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VisitScheduleCommandService {

    private final VisitScheduleCommandMapper visitScheduleCommandMapper;

    @Transactional
    public void startVisit(Long vsId, StartVisitRequest request) {
        int updated = visitScheduleCommandMapper.updateVisitStatusToInProgress(
                vsId,
                request.getActualStartTime()
        );

        if (updated == 0) {
            throw new IllegalArgumentException("해당 일정을 찾을 수 없거나 이미 시작되었습니다.");
        }
    }

    @Transactional
    public void completeVisit(Long vsId, CompleteVisitRequest request) {
        int updated = visitScheduleCommandMapper.updateVisitStatusToCompleted(
                vsId,
                request.getActualEndTime()
        );

        if (updated == 0) {
            throw new IllegalArgumentException("해당 일정을 찾을 수 없거나 이미 완료되었습니다.");
        }
    }
}
