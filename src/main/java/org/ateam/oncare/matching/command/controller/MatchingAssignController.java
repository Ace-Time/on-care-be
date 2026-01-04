package org.ateam.oncare.matching.command.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.matching.command.dto.AssignMatchingRequest;
import org.ateam.oncare.matching.command.service.MatchingAssignService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching/assign")
public class MatchingAssignController {

    private final MatchingAssignService matchingAssignService;

    @PostMapping
    public ResponseEntity<Void> assign(@Valid @RequestBody AssignMatchingRequest req) {
        matchingAssignService.assign(
                req.getBeneficiaryId(),
                req.getCareWorkerId(),
                req.getEffectiveDate()
        );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{beneficiaryId}")
    public ResponseEntity<Void> unassign(
            @PathVariable Long beneficiaryId,
            @RequestParam("effectiveDate") LocalDate effectiveDate
    ) {
        matchingAssignService.unassign(beneficiaryId, effectiveDate);
        return ResponseEntity.noContent().build();
    }
}