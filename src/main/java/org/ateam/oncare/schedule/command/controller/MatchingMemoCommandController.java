package org.ateam.oncare.schedule.command.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.command.dto.MatchingMemoUpsertRequest;
import org.ateam.oncare.schedule.command.dto.MatchingMemoUpsertResponse;
import org.ateam.oncare.schedule.command.service.MatchingMemoCommandService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule/memo")
@RequiredArgsConstructor
public class MatchingMemoCommandController {

    private final MatchingMemoCommandService matchingMemoCommandService;

    @PutMapping
    public void upsertMemo(
            @RequestBody @Valid MatchingMemoUpsertRequest request
    ) {
        matchingMemoCommandService.upsertMemo(request);
    }
}