package org.ateam.oncare.rental.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.rental.command.dto.RentalContractForCalculationDTO;
import org.ateam.oncare.rental.command.dto.RequestContractDTO;
import org.ateam.oncare.rental.command.dto.ResponseContractRentalDTO;
import org.ateam.oncare.rental.query.mapper.RentalContractMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalQueryServiceImpl implements RentalQueryService {
    private final RentalContractMapper rentalContractMapper;


    @Override
    public Slice<ResponseContractRentalDTO> getContract(RequestContractDTO condition, Pageable pageable) {
        List<ResponseContractRentalDTO> response = rentalContractMapper.selectRentalContract(condition,pageable);

        boolean hasNext = false;
        if(response.size() > pageable.getPageSize()){
            hasNext = true;
            response.remove(pageable.getPageSize());
        }

        log.debug("getContract: response={}",response);
        return new SliceImpl<>(response,pageable,hasNext);
    }

    @Override
    public List<RentalContractForCalculationDTO> getTargetRentalContracts(LocalDate calcDate) {
        List<RentalContractForCalculationDTO> targetRentalContracts
                = rentalContractMapper.selectRentalContractForCalculation(calcDate);

        log.debug("targetRentalContracts:{}",targetRentalContracts);

        return targetRentalContracts;
    }
}
