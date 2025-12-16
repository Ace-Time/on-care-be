package org.ateam.oncare.schedule.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.query.dto.MatchingMemoDto;
import org.ateam.oncare.schedule.query.service.MatchingMemoService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/schedule/memo")
@RequiredArgsConstructor
public class MatchingMemoController {

    private final MatchingMemoService matchingMemoQueryService;

    @GetMapping
    public MatchingMemoDto getMemo(
            @RequestParam Long matchingId,
            @RequestParam LocalDate date
    ) {
        return matchingMemoQueryService.getMemo(matchingId, date);
    }
}