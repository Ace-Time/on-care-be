package org.ateam.oncare.schedule.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.command.dto.ConfirmedVisitScheduleTimeUpdateRequest;
import org.ateam.oncare.schedule.command.dto.ConfirmedVisitScheduleTimeUpdateResponse;
import org.ateam.oncare.schedule.command.service.ConfirmedVisitScheduleCommandService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/confirmed-calendar/visit-schedules")
public class ConfirmedVisitScheduleCommandController {

    private final ConfirmedVisitScheduleCommandService confirmedVisitScheduleCommandService;

    @PutMapping("/{vsId}/time")
    public ConfirmedVisitScheduleTimeUpdateResponse updateTime(
            @PathVariable Long vsId,
            @RequestBody ConfirmedVisitScheduleTimeUpdateRequest request
    ) {
        return confirmedVisitScheduleCommandService.updateTime(vsId, request);
    }

    @DeleteMapping("/{vsId}")
    public void delete(@PathVariable Long vsId) {
        confirmedVisitScheduleCommandService.delete(vsId);
    }
}