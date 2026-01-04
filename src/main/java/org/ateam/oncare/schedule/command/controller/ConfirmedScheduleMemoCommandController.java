package org.ateam.oncare.schedule.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.command.dto.ConfirmedScheduleMemoUpsertRequest;
import org.ateam.oncare.schedule.command.dto.ConfirmedScheduleMemoUpsertResponse;
import org.ateam.oncare.schedule.command.service.ConfirmedScheduleMemoCommandService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/confirmed-calendar")
public class ConfirmedScheduleMemoCommandController {

    private final ConfirmedScheduleMemoCommandService confirmedScheduleMemoCommandService;

    @PutMapping("/memo")
    public ConfirmedScheduleMemoUpsertResponse upsert(@RequestBody ConfirmedScheduleMemoUpsertRequest request) {
        return confirmedScheduleMemoCommandService.upsert(request);
    }
}