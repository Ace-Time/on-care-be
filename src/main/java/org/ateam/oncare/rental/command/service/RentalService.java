package org.ateam.oncare.rental.command.service;

import org.apache.ibatis.javassist.NotFoundException;
import org.ateam.oncare.careproduct.command.dto.ProductAmountForRentalDTO;
import org.ateam.oncare.config.customexception.NotFoundProductMasterException;
import org.ateam.oncare.rental.command.dto.*;
import org.ateam.oncare.rental.command.entity.RentalProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RentalService {
    Map<String, Long> getExpectedToShip();

    List<ResponseContractStatusType> getContractStatusType();

    Slice<ResponseContractRentalDTO> getContract(RequestContractDTO condition, Pageable pageable);

    void calcRentalAmount(List<RentalContractForCalculationDTO> targetRentalContracts);

    Map<Long,List<RentalProductForCalculationDTO>> selectRentalProduct(List<Long> contratIdList);

    List<ResponseRentalProductDTO> getProductRental(Long contractCode);

    List<ResponseRetnalProductStatusType> getRentalProductStatus();

    ResponseRentalContractDTO registRentalContract(RequestRentalContractDTO request);
}
