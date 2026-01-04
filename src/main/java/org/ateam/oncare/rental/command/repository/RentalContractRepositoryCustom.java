package org.ateam.oncare.rental.command.repository;

import com.querydsl.core.Tuple;
import org.ateam.oncare.rental.command.dto.RentalContractForCalculationDTO;

import java.util.List;
import java.util.Map;

public interface RentalContractRepositoryCustom {
    Map<String, Long> selectExpectedToShip();

    void calculateContratMonthlyForRental(List<RentalContractForCalculationDTO> targetRentalContracts);
}
