package org.ateam.oncare.beneficiary.command.service;


import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.command.dto.response.RentalContractCompleteResponse;
import org.ateam.oncare.beneficiary.command.mapper.BeneficiaryRentalContractMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BeneficiaryRentalContractService {

    private final BeneficiaryRentalContractMapper mapper;

    @Transactional
    public RentalContractCompleteResponse completeRentalContract(
            Long beneficiaryId,
            Long rentalContractId
    ) {
        int updated = mapper.completeRentalContract(beneficiaryId, rentalContractId);

        if (updated == 0) {
            return RentalContractCompleteResponse.builder()
                    .success(false)
                    .message("계약 상태가 종료 가능한 상태가 아니거나 존재하지 않습니다.")
                    .beneficiaryId(beneficiaryId)
                    .rentalContractId(rentalContractId)
                    .build();
        }

        return RentalContractCompleteResponse.builder()
                .success(true)
                .message("렌탈 계약이 정상적으로 종료되었습니다.")
                .beneficiaryId(beneficiaryId)
                .rentalContractId(rentalContractId)
                .contractStatus("종료")
                .endDate(java.time.LocalDate.now().toString())
                .build();
    }
}