package org.ateam.oncare.counsel.command.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.counsel.command.dto.*;
import org.ateam.oncare.counsel.command.service.CounselFacadeService;
import org.ateam.oncare.counsel.command.service.CustomerFacadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;


@RestController
@RequestMapping("/api/counsel")
@Slf4j
@Validated
@RequiredArgsConstructor
public class CounselCommandController {

    private final CounselFacadeService counselFacadeService;
    private final CustomerFacadeService customerFacadeService;

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

    @PostMapping("/potentialCustomer")
    public ResponseEntity<PotentialCustomerResponse> registPotentialCustomer(
            @RequestBody RegistPotentialCustomer request
    ) {
        return counselFacadeService.registPotentialCustomer(request);
    }

    // 가입 상담 단계별 저장
    @PostMapping("/potentialStage/{stage}/{customerId}")
    public ResponseEntity<SaveStageDataResponse> saveStageData(
            @PathVariable Integer stage,
            @PathVariable BigInteger customerId,
            @RequestBody StageData request) {

        // StageData에 stage와 customerId 설정
        request.setStage(stage);
        request.setPotentialId(customerId.longValue());

        return counselFacadeService.saveStageData(request);
    }

    // 가입 상담 단계별 데이터 조회
    @GetMapping("/potentialStage/{customerId}")
    public ResponseEntity<Map<Integer, StageData>> getStageData(@PathVariable BigInteger customerId) {
        return counselFacadeService.getStageData(customerId.longValue());
    }

    // 신규 수급자 등록
    @PostMapping("/regist/newBeneficiary")
    public ResponseEntity<RegistNewBeneficiaryResponse> registNewBeneficiary(@RequestBody RegistNewBeneficiary request) {
        return customerFacadeService.registNewBeneficiary(request);
    }

    // 수급자 특이사항 추가
    @PostMapping("/beneficiaries/{beneficiaryId}/significants/{significantId}")
    public ResponseEntity<Void> addBeneficiarySignificant(
            @PathVariable BigInteger beneficiaryId,
            @PathVariable Integer significantId) {
        customerFacadeService.addBeneficiarySignificant(beneficiaryId, significantId);
        return ResponseEntity.ok().build();
    }


    // 수급자 특이사항 삭제
    @DeleteMapping("/beneficiaries/{beneficiaryId}/significants/{significantId}")
    public ResponseEntity<Void> deleteBeneficiarySignificant(
            @PathVariable BigInteger beneficiaryId,
            @PathVariable Integer significantId
    ) {
        customerFacadeService.deleteBeneficiarySignificant(beneficiaryId, significantId);
        return ResponseEntity.noContent().build();
    }

}
