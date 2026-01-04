package org.ateam.oncare.rental.command.repository;

import org.ateam.oncare.rental.command.dto.RentalProductForCalculationDTO;
import org.ateam.oncare.rental.command.entity.RentalProduct;

import java.util.List;
import java.util.Map;

public interface RentalProductRepositoryCustom {
    Map<Long,List<RentalProductForCalculationDTO>> selectRentalProduct(List<Long> contratIdList);
}
