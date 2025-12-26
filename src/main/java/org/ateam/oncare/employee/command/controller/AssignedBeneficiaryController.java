package org.ateam.oncare.employee.command.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.employee.command.dto.AssignedBeneficiaryDTO;
import org.ateam.oncare.employee.command.service.AssignedBeneficiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignedBeneficiary")
public class AssignedBeneficiaryController {

    private final AssignedBeneficiaryService assignedBeneficiaryService;

    @GetMapping("/care-workers/{careWorkerId}/beneficiaries")
    public ResponseEntity<List<AssignedBeneficiaryDTO>> getAssignedBeneficiaries(
            @PathVariable Long careWorkerId) {

        return ResponseEntity.ok(
                assignedBeneficiaryService.getAssignedBeneficiaries(careWorkerId));
    }
}