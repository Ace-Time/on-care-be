package org.ateam.oncare.counsel.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.beneficiary.command.service.BeneficiaryUpdateService;
import org.ateam.oncare.counsel.command.dto.GeneralCounselResponse;
import org.ateam.oncare.counsel.command.dto.RegistGeneralCounselRequest;
import org.ateam.oncare.counsel.command.entity.CounselHistory;
import org.ateam.oncare.counsel.command.repository.CounselHistoryRepository;
import org.ateam.oncare.counsel.command.repository.PotentialCustomerRepository;
import org.ateam.oncare.counsel.command.repository.PotentialStageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CounselCommandServiceImpl implements CounselCommandService {
    private final CounselHistoryRepository counselHistoryRepository;
    private final PotentialCustomerRepository potentialCustomerRepository;
    private final PotentialStageRepository potentialStageRepository;
    private final BeneficiaryUpdateService beneficiaryUpdateService;

    @Override
    public GeneralCounselResponse registGeneralCounsel(RegistGeneralCounselRequest request) {
        CounselHistory counselHistory = saveCounselHistory(
                request.getConsultDate(),
                request.getSummary(),
                request.getDetail(),
                request.getGuardianSt(),
                request.getFollowUp(),
                request.getFollowUpNecessary(),
                request.getChurn(),
                request.getChurnReason(),
                request.getReservationChannelId(),
                request.getCounselCategoryId(),
                request.getCustomerId(),
                request.getCustomerType(),
                request.getEmployeeId()
        );
        return GeneralCounselResponse.from(counselHistory);
    }

    private CounselHistory saveCounselHistory(LocalDateTime consultDate, String summary, String detail, int guardianSt,
                                              String followUp, String followUpNecessary, String churn, String churnReason,
                                              int reservationChannelId, Integer counselCategoryId, BigInteger customerId,
                                              String customerType, int employeeId) {
        CounselHistory.CounselHistoryBuilder counselHistoryBuilder = CounselHistory.builder()
                .consultDate(consultDate)
                .summary(summary)
                .detail(detail)
                .guardianSt(guardianSt)
                .followUp(followUp)
                .followUpNecessary(followUpNecessary)
                .churn(churn)
                .churnReason(churnReason)
                .reservationChannelId(reservationChannelId)
                .counselCategoryId(counselCategoryId)
                .counselorId(employeeId);

        if ("beneficiary".equalsIgnoreCase(customerType)) {
            // 수급자라면 -> beneficiaryId 칸에 넣기
            counselHistoryBuilder.beneficiaryId(customerId.longValue());
            counselHistoryBuilder.potentialId(null); // 잠재고객 칸은 비움
            beneficiaryUpdateService.updateLastCounselDate(customerId.longValue(), consultDate);

        } else if ("potential".equalsIgnoreCase(customerType)) {
            // 잠재고객이라면 -> potentialId 칸에 넣기
            counselHistoryBuilder.potentialId(customerId.longValue());
            counselHistoryBuilder.beneficiaryId(null); // 수급자 칸은 비움
            potentialCustomerRepository.updateLastCounselDate(customerId.longValue(), consultDate);
        }
//        else {
//            // 타입이 이상하면 에러 처리 = 내가 Exception 만들어서 처리하기
//            throw new CustomException(ErrorCode.INVALID_CUSTOMER_TYPE);
//        }
        return counselHistoryRepository.save(counselHistoryBuilder.build());
    }


}
