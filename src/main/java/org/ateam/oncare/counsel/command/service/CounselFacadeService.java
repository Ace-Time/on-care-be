package org.ateam.oncare.counsel.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.beneficiary.command.service.BeneficiaryRegistService;
import org.ateam.oncare.beneficiary.query.service.BeneficiaryDetailService;
import org.ateam.oncare.counsel.command.dto.*;
import org.ateam.oncare.counsel.command.entity.CounselHistory;
import org.ateam.oncare.counsel.command.repository.PotentialCustomerRepository;
import org.ateam.oncare.counsel.query.service.CounselQueryService;
import org.ateam.oncare.employee.command.repository.BeneficiaryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CounselFacadeService {
    private final CounselRegistrationService counselRegistrationService;
    private final PotentialStageService potentialStageService;
    private final PotentialCustomerService potentialCustomerService;
    private final CounselQueryService counselQueryService;
    private final BeneficiaryRegistService beneficiaryRegistService;
    private final BeneficiaryRepository beneficiaryRepository;
    private final PotentialCustomerRepository potentialCustomerRepository;

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
    @Transactional
    public ResponseEntity<SaveStageDataResponse> saveStageData(StageData request) {
        potentialStageService.updateStageData(request);
        return ResponseEntity.ok(new SaveStageDataResponse());
    }

    // 가입 상담 단계별 데이터 조회
    @Transactional(readOnly = true)
    public ResponseEntity<Map<Integer, StageData>> getStageData(Long potentialCustomerId) {
        Map<Integer, StageData> stageDataMap = potentialStageService.findStageDataByPotentialId(potentialCustomerId);
        return ResponseEntity.ok(stageDataMap);
    }


    // 잠재 고객만 등록
    public ResponseEntity<PotentialCustomerResponse> registPotentialCustomer(RegistPotentialCustomer request) {
        // 전화번호 중복 체크
        String normalizedPhone = request.getPhone().replace("-", "");
        // beneficiary 체크
        if (beneficiaryRepository.existsByPhoneNormalized(normalizedPhone)) {
            throw new IllegalArgumentException("이미 등록된 기존고객입니다.");
        }
        // potential_customer 체크
        if (potentialCustomerRepository.existsByPhoneNormalized(normalizedPhone)) {
            throw new IllegalArgumentException("이미 등록된 잠재고객입니다.");
        }
        BigInteger potentialId = potentialCustomerService.registPotentialCustomer(request.getName(), request.getPhone());
        PotentialCustomerResponse response = new PotentialCustomerResponse();
        response.setCustomerId(potentialId.longValue());
        response.setCustomerType("POTENTIAL");
        response.setName(request.getName());
        response.setPhone(request.getPhone());
        return ResponseEntity.ok(response);
    }



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

    public Map<String, Object> checkDuplicateCustomer(String phone) {
        Map<String, Object> result = new HashMap<>();

        // 전화번호 정규화 (하이픈 제거)
        String normalizedPhone = phone.replace("-", "");

        // 1. beneficiary 테이블에서 먼저 확인 (기존고객 우선)
        boolean existsInBeneficiary = beneficiaryRepository.existsByPhoneNormalized(normalizedPhone);

        if (existsInBeneficiary) {
            result.put("exists", true);
            result.put("type", "beneficiary");
            return result;
        }

        // 2. potential_customer 테이블에서 확인
        boolean existsInPotential = potentialCustomerRepository.existsByPhoneNormalized(normalizedPhone);

        if (existsInPotential) {
            result.put("exists", true);
            result.put("type", "potential");
            return result;
        }

        // 3. 중복 없음
        result.put("exists", false);
        return result;
    }
}
