package org.ateam.oncare.careproduct.command.repository;

import org.ateam.oncare.careproduct.command.dto.RequestPlannedStockInOutDTO;
import org.ateam.oncare.careproduct.command.dto.ResponsePlannedStockInOutDTO;
import org.ateam.oncare.careproduct.command.entity.ProductStatus;
import org.ateam.oncare.careproduct.command.entity.ProductTask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ProductTaskRepositoryCustom {
    ProductTask selectByProductId(String productId);

    Slice<ResponsePlannedStockInOutDTO> selectExpectedStock(RequestPlannedStockInOutDTO condition, Pageable pageable);
}
