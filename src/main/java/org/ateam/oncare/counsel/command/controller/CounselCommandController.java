package org.ateam.oncare.counsel.command.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.counsel.command.dto.*;
import org.ateam.oncare.counsel.command.service.CounselFacadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;


@RestController
@RequestMapping("/api/counsel")
@Slf4j
@Validated
@RequiredArgsConstructor
public class CounselCommandController {

    private final CounselFacadeService counselFacadeService;
    // 고객 리스트에 나오지 않은 신규 고객일 경우
    @PostMapping("/subscription")  // 가입 상담
    public ResponseEntity<NewSubscriptionResponse> registNewSubscription(@RequestBody Subscription request) {
        return counselFacadeService.registNewSubscription(request);
    }

    @PostMapping("/general")       // 통합 상담(렌탈 + 문의 + 컴플레인 + 해지)
    public ResponseEntity<GeneralCounselResponse> registNewGeneralCounsel(@RequestBody GeneralCounsel request) {
        return counselFacadeService.registNewGeneralCounsel(request);
    }

     // 기존 고객의 상담이 진행될 경우
    @PostMapping("/{customerId}/subscription") // 기존 고객의 가입 상담
    public ResponseEntity<SubscriptionResponse> registSubscription(@RequestBody Subscription request,
                                                                      @PathVariable BigInteger customerId,
                                                                      @RequestParam("customerType") String customerType,
                                                                      @RequestParam("customerCategoryName") String customerCategoryName) {
        return counselFacadeService.registSubscription(request, customerId, customerType, customerCategoryName);
    }

    @PostMapping("/{customerId}/general")  // 기존 고객의 통합 상담
    public ResponseEntity<GeneralCounselResponse> registGeneralCounsel(@RequestBody GeneralCounsel request,
                                                                       @PathVariable BigInteger customerId,
                                                                       @RequestParam("customerType") String customerType) {
        return counselFacadeService.registGeneralCounsel(request);
    }

    // 가입 상담 단계별 저장 -> 프론트 구현 후 작성
//    @PostMapping("/potentialStage/{stage}/{customerId}")
//    public ResponseEntity<SaveStageDataResponse> saveStageData(@RequestBody StageData request,
//                                                               @PathVariable int stage,
//                                                               @PathVariable BigInteger customerId,
//                                                               @RequestParam String customerType) {
//        return counselFacadeService.saveStageData(request, stage, customerId, customerType);
//    }



}
