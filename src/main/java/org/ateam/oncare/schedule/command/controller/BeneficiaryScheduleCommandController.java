package org.ateam.oncare.schedule.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.command.dto.BeneficiaryScheduleCreateRequest;
import org.ateam.oncare.schedule.command.dto.BeneficiaryScheduleResponse;
import org.ateam.oncare.schedule.command.dto.BeneficiaryScheduleUpdateRequest;
import org.ateam.oncare.schedule.command.service.BeneficiaryScheduleCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/beneficiary-schedules")
public class BeneficiaryScheduleCommandController {

    private final BeneficiaryScheduleCommandService service;

    @PostMapping
    public ResponseEntity<BeneficiaryScheduleResponse> create(@RequestBody BeneficiaryScheduleCreateRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeneficiaryScheduleResponse> update(
            @PathVariable Long id,
            @RequestBody BeneficiaryScheduleUpdateRequest req
    ) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam("effectiveDate") LocalDate effectiveDate
    ) {
        service.delete(id, effectiveDate);
        return ResponseEntity.noContent().build();
    }
}