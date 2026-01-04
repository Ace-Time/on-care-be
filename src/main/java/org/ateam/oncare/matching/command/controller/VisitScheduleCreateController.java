package org.ateam.oncare.matching.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.matching.command.dto.CreateVisitScheduleRequest;
import org.ateam.oncare.matching.command.service.VisitScheduleCreateService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching/visit-schedules")
public class VisitScheduleCreateController {

    private final VisitScheduleCreateService visitScheduleCreateService;

    @PostMapping
    public void create(@RequestBody CreateVisitScheduleRequest request) {
        visitScheduleCreateService.create(request);
    }
}