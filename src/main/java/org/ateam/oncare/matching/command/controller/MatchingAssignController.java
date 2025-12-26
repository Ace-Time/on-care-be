package org.ateam.oncare.matching.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.matching.command.dto.AssignMatchingRequest;
import org.ateam.oncare.matching.command.service.MatchingAssignService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matching/assign")
public class MatchingAssignController {

    private final MatchingAssignService matchingAssignService;

    @PostMapping
    public void assign(@RequestBody AssignMatchingRequest req) {
        matchingAssignService.assign(req.getBeneficiaryId(), req.getCareWorkerId());
    }
}