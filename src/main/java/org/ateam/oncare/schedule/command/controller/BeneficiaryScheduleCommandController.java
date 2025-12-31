package org.ateam.oncare.schedule.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.schedule.command.dto.BeneficiaryScheduleCreateRequest;
import org.ateam.oncare.schedule.command.dto.BeneficiaryScheduleResponse;
import org.ateam.oncare.schedule.command.dto.BeneficiaryScheduleUpdateRequest;
import org.ateam.oncare.schedule.command.service.BeneficiaryScheduleCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/beneficiary-schedules")
public class BeneficiaryScheduleCommandController {

    private final BeneficiaryScheduleCommandService service;

    // 생성
    @PostMapping
    public ResponseEntity<BeneficiaryScheduleResponse> create(
            @RequestBody BeneficiaryScheduleCreateRequest req
    ) {
        return ResponseEntity.ok(service.create(req));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<BeneficiaryScheduleResponse> update(
            @PathVariable Long id,
            @RequestBody BeneficiaryScheduleUpdateRequest req
    ) {
        return ResponseEntity.ok(service.update(id, req));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}