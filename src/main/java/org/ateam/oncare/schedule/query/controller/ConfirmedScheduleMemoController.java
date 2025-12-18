package org.ateam.oncare.schedule.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.ateam.oncare.schedule.query.dto.ConfirmedScheduleMemoDto;
import org.ateam.oncare.schedule.query.service.ConfirmedScheduleMemoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/confirmed-calendar")
public class ConfirmedScheduleMemoController {

    private final ConfirmedScheduleMemoService confirmedScheduleMemoService;

    @GetMapping("/memo")
    public ConfirmedScheduleMemoDto getMemo(@RequestParam("vsId") Integer vsId) {
        return confirmedScheduleMemoService.getMemo(vsId);
    }
}