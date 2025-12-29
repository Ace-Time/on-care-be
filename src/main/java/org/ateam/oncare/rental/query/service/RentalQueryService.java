package org.ateam.oncare.rental.query.service;

import org.ateam.oncare.rental.command.dto.RentalContractForCalculationDTO;
import org.ateam.oncare.rental.command.dto.RequestContractDTO;
import org.ateam.oncare.rental.command.dto.ResponseContractRentalDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

public interface RentalQueryService {
    Slice<ResponseContractRentalDTO> getContract(RequestContractDTO condition, Pageable pageable);

    List<RentalContractForCalculationDTO> getTargetRentalContracts(LocalDate calcDate);
}
