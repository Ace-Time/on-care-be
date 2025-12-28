package org.ateam.oncare.matching.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.matching.command.dto.ChangeConfirmedVisitCareWorkerRequest;
import org.ateam.oncare.matching.command.dto.ChangeMatchingCareWorkerRequest;
import org.ateam.oncare.matching.command.service.MatchingCareWorkerChangeService;
import org.ateam.oncare.matching.command.service.VisitScheduleCareWorkerChangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/change")
public class MatchingCareWorkerChangeController {

    private final MatchingCareWorkerChangeService matchingCareWorkerChangeService;
    private final VisitScheduleCareWorkerChangeService visitScheduleCareWorkerChangeService;

    // 매칭 요양보호사 변경
    @PatchMapping("/matchings/{matchingId}/care-worker")
    public ResponseEntity<Void> changeMatchingCareWorker(
            @PathVariable Long matchingId,
            @RequestBody ChangeMatchingCareWorkerRequest request
    ) {
        matchingCareWorkerChangeService.changeCareWorker(matchingId, request);
        return ResponseEntity.ok().build();
    }

    // 방문일정(confirmed) 요양보호사 변경
    @PatchMapping("/visit-schedules/{vsId}/care-worker")
    public ResponseEntity<Void> changeVisitScheduleCareWorker(
            @PathVariable Long vsId,
            @RequestBody ChangeConfirmedVisitCareWorkerRequest request
    ) {
        visitScheduleCareWorkerChangeService.changeCareWorker(vsId, request);
        return ResponseEntity.ok().build();
    }
}