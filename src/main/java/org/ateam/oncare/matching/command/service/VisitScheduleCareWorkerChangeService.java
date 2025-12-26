package org.ateam.oncare.matching.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.careworker.command.repository.CareWorkerRepository;
import org.ateam.oncare.matching.command.dto.ChangeConfirmedVisitCareWorkerRequest;
import org.ateam.oncare.matching.command.repository.VisitScheduleCareWorkerChangeRepository;
import org.ateam.oncare.schedule.command.entity.VisitSchedule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VisitScheduleCareWorkerChangeService {

    private final VisitScheduleCareWorkerChangeRepository visitScheduleCareWorkerChangeRepository;
    private final CareWorkerRepository careWorkerRepository;

    @Transactional
    public void changeCareWorker(Long vsId, ChangeConfirmedVisitCareWorkerRequest request) {

        VisitSchedule visitSchedule = visitScheduleCareWorkerChangeRepository.findById(vsId)
                .orElseThrow(() ->
                        new IllegalArgumentException("방문일정이 존재하지 않습니다. vsId=" + vsId)
                );

        Long careWorkerId = request.getCareWorkerId();
        if (careWorkerId == null) {
            throw new IllegalArgumentException("careWorkerId는 필수입니다.");
        }

        // 존재 검증만
        careWorkerRepository.findById(careWorkerId)
                .orElseThrow(() ->
                        new IllegalArgumentException("요양보호사가 존재하지 않습니다. careWorkerId=" + careWorkerId)
                );

        // FK 값만 변경
        visitSchedule.setCareWorkerId(careWorkerId);
    }
}