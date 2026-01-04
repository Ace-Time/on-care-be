package org.ateam.oncare.careproduct.command.repository;

import org.ateam.oncare.careproduct.command.dto.AggregationOfProductDTO;
import org.ateam.oncare.careproduct.command.dto.RequestProductForSelectDTO;
import org.ateam.oncare.careproduct.command.dto.RequestProductMasterForSelectDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductDTO;
import org.ateam.oncare.rental.command.dto.RentalProductForCalculationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ProductRepositoryCustom {
    List<AggregationOfProductDTO> selectAggregationOfProduct(List<String> masterCodes);

    Slice<ResponseProductDTO> getProduct(RequestProductForSelectDTO condition, Pageable pageable);

    void calculationRentalFee(List<RentalProductForCalculationDTO> calcProductRentalFeeList);
}
