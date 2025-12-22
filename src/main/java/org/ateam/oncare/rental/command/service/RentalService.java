package org.ateam.oncare.rental.command.service;

import org.ateam.oncare.rental.command.dto.RequestContractDTO;
import org.ateam.oncare.rental.command.dto.ResponseContractRentalDTO;
import org.ateam.oncare.rental.command.dto.ResponseContractStatusType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;

import java.util.List;
import java.util.Map;

public interface RentalService {
    Map<String, Long> getExpectedToShip();

    List<ResponseContractStatusType> getContractStatusType();

    Slice<ResponseContractRentalDTO> getContract(RequestContractDTO condition, Pageable pageable);
}
