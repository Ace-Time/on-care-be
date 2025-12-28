package org.ateam.oncare.counsel.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.beneficiary.query.service.BeneficiaryDetailService;
import org.ateam.oncare.counsel.command.dto.*;
import org.ateam.oncare.counsel.command.entity.CounselHistory;
import org.ateam.oncare.counsel.query.service.CounselQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CounselFacadeService {
    private final CounselRegistrationService counselRegistrationService;
    private final PotentialStageService potentialStageService;
    private final PotentialCustomerService potentialCustomerService;
    private final CounselQueryService counselQueryService;

    @Transactional
    public ResponseEntity<NewSubscriptionResponse> registNewSubscription(Subscription request) {
        // 신규 잠재고객 등록 + Id값 받아오기 (필수)
        BigInteger potentialId = potentialCustomerService.registPotentialCustomer(request.getName(), request.getPhone());
        request.setCustomerId(potentialId);
        request.setCustomerType("POTENTIAL");

        // 가입상담 관리 1단계 처리
        potentialStageService.registPotentialStage(request,potentialId);

        // 상담 이력에 저장, 응답 ResponseDTO 조립
        NewSubscriptionResponse newSubscriptionResponse =
                buildNewSubscriptionResponse(counselRegistrationService.registSubscription(request));
        return  ResponseEntity.ok(newSubscriptionResponse);
    }

    @Transactional
    public ResponseEntity<GeneralCounselResponse> registNewGeneralCounsel(GeneralCounsel request) {
        // 신규 잠재 고객 등록
        BigInteger potentialId = potentialCustomerService.registPotentialCustomer(request.getName(), request.getPhone());
        request.setCustomerId(potentialId);
        request.setCustomerType("POTENTIAL");

        // 신규 상담 이력으로 저장
        GeneralCounselResponse generalCounselResponse = counselRegistrationService.registGeneralCounsel(request);
        return ResponseEntity.ok(generalCounselResponse);
    }

    @Transactional
    public ResponseEntity<SubscriptionResponse> registSubscription(Subscription request, BigInteger customerId,
                                                                   String customerType, String customerCategoryName) {
        SubscriptionResponse response = switch (customerCategoryName) {
            case "잠재고객" -> {        // 잠재 고객의 가입상담 = 이전 상담 내역에서 이어서 진행
                Long potentialId = customerId.longValue();
                Map<Integer, StageData> stageData = potentialStageService.findStageDataByPotentialId(potentialId);

                SubscriptionResponse res = buildSubscriptionResponse(counselRegistrationService.registSubscription(request));
                res.setStageData(stageData);
                yield res;
            }

            case "기존고객" -> {       // 기존 고객의 가입상담 = 서비스 재가입
                Long beneficiaryId = customerId.longValue();
                Long potentialId = counselQueryService.findPotentialIdByBeneficiaryId(beneficiaryId);

                yield buildSubscriptionResponse(counselRegistrationService.registSubscription(request));
            }

            case "이탈고객" -> {        // 이탈+잠재 고객의 가입상담 = 이어서 진행, 이탈+기존 고객의 가입상담 = 서비스 재가입
                if(customerType.equals("POTENTIAL")) {
                    Long potentialId = customerId.longValue();
                    yield buildSubscriptionResponse(counselRegistrationService.registSubscription(request));
                } else if(customerType.equals("BENEFICIARY")) {
                    Long beneficiaryId = customerId.longValue();
                    yield buildSubscriptionResponse(counselRegistrationService.registSubscription(request));
                } else {
                    throw new IllegalArgumentException("이탈 고객의 상세 타입이 잘못되었습니다: " + customerType);
                }
            }
            default -> throw new IllegalArgumentException("고객 카테고리를 다시 확인해주세요: " + customerCategoryName);
        };
        return ResponseEntity.ok(response);
    }

    // 기존 고객의 통합 상담(렌탈 + 문의 + 불만) 등록
    public ResponseEntity<GeneralCounselResponse> registGeneralCounsel(GeneralCounsel request) {
        GeneralCounselResponse generalCounselResponse = counselRegistrationService.registGeneralCounsel(request);
        return ResponseEntity.ok(generalCounselResponse);
    }

    // 기존 고객의 해지 상담 등록


    // 가입 상담 단계별 저장
//    public ResponseEntity<SaveStageDataResponse> saveStageData(StageData request, int stage, BigInteger customerId, String customerType) {
//
//    }

    private NewSubscriptionResponse buildNewSubscriptionResponse(CounselHistory counselHistory) {
        NewSubscriptionResponse response = new NewSubscriptionResponse();
        response.setCounselHistoryId(BigInteger.valueOf(counselHistory.getId()));
        response.setCounselCategoryId(counselHistory.getCounselCategoryId());
        response.setDetail(counselHistory.getDetail());
        response.setSummary(counselHistory.getSummary());
        response.setFollowUp(counselHistory.getFollowUp());
        response.setFollowUpNecessary(counselHistory.getFollowUpNecessary());
        response.setChurn(counselHistory.getChurn());
        response.setChurnReason(counselHistory.getChurnReason());
        response.setCounselorId(counselHistory.getCounselorId());
        response.setConsultDate(counselHistory.getConsultDate());
        response.setReservationChannelId(counselHistory.getReservationChannelId());
        if(counselHistory.getBeneficiaryId() != null) {
            response.setBeneficiaryId(BigInteger.valueOf(counselHistory.getBeneficiaryId()));
            response.setPotentialId(null);
        } else {
            response.setBeneficiaryId(null);
            response.setPotentialId(BigInteger.valueOf(counselHistory.getPotentialId()));
        }
        return response;
    }


    private SubscriptionResponse buildSubscriptionResponse(CounselHistory counselHistory) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setCounselHistoryId(BigInteger.valueOf(counselHistory.getId()));
        response.setCounselCategoryId(counselHistory.getCounselCategoryId());
        response.setDetail(counselHistory.getDetail());
        response.setSummary(counselHistory.getSummary());
        response.setFollowUp(counselHistory.getFollowUp());
        response.setFollowUpNecessary(counselHistory.getFollowUpNecessary());
        response.setChurn(counselHistory.getChurn());
        response.setChurnReason(counselHistory.getChurnReason());
        response.setCounselorId(counselHistory.getCounselorId());
        response.setConsultDate(counselHistory.getConsultDate());
        response.setReservationChannelId(counselHistory.getReservationChannelId());
        if(counselHistory.getBeneficiaryId() != null) {
            response.setBeneficiaryId(BigInteger.valueOf(counselHistory.getBeneficiaryId()));
            response.setPotentialId(null);
        } else {
            response.setBeneficiaryId(null);
            response.setPotentialId(BigInteger.valueOf(counselHistory.getPotentialId()));
        }
        return response;
    }

}
