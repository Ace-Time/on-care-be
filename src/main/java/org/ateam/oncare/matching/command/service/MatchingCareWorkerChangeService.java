package org.ateam.oncare.matching.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.careworker.command.repository.CareWorkerRepository;
import org.ateam.oncare.matching.command.dto.ChangeMatchingCareWorkerRequest;
import org.ateam.oncare.matching.command.repository.MatchingCareWorkerChangeRepository;
import org.ateam.oncare.schedule.command.entity.Matching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingCareWorkerChangeService {

    private final MatchingCareWorkerChangeRepository matchingCareWorkerChangeRepository;
    private final CareWorkerRepository careWorkerRepository;

    @Transactional
    public void changeCareWorker(Long matchingId, ChangeMatchingCareWorkerRequest request) {
        Matching matching = matchingCareWorkerChangeRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("매칭이 존재하지 않습니다. matchingId=" + matchingId));

        Long careWorkerId = request.getCareWorkerId();
        if (careWorkerId == null) {
            throw new IllegalArgumentException("careWorkerId는 필수입니다.");
        }

        if (!careWorkerRepository.existsById(careWorkerId)) {
            throw new IllegalArgumentException("요양보호사가 존재하지 않습니다. careWorkerId=" + careWorkerId);
        }

        matching.setCareWorkerId(careWorkerId);
    }
}