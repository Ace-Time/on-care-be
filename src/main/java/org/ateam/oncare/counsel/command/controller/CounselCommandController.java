package org.ateam.oncare.counsel.command.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.counsel.command.dto.RegistGeneralCounselRequest;
import org.ateam.oncare.counsel.command.dto.GeneralCounselResponse;
import org.ateam.oncare.counsel.command.dto.RegistSubscriptionCounselRequest;
import org.ateam.oncare.counsel.command.dto.SubscriptionCounselResponse;
import org.ateam.oncare.counsel.command.service.CounselCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/counsel")
@Slf4j
@Validated
@RequiredArgsConstructor
public class CounselCommandController {

    private final CounselCommandService counselCommandService;

    @PostMapping("/general")
    public ResponseEntity<GeneralCounselResponse> registGeneralCounsel(
            @RequestBody RegistGeneralCounselRequest request
    ) {

        return ResponseEntity.ok(counselCommandService.registGeneralCounsel(request));
    }

//    @PostMapping("/subscription")
//    public ResponseEntity<SubscriptionCounselResponse> registSubscrptionCounsel(
//            @RequestBody RegistSubscriptionCounselRequest request
//    ) {
//        return ResponseEntity.ok(counselCommandService.registSubscriptionCounsel());
//    }
}
