package org.ateam.oncare.careproduct.command.service;

import org.ateam.oncare.careproduct.command.dto.*;
import org.ateam.oncare.global.eventType.ProductStockEvent;
import org.ateam.oncare.rental.command.dto.RentalContractForCalculationDTO;
import org.ateam.oncare.rental.command.dto.RentalProductForCalculationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Slice<ResponseProductDTO> getProduct(RequestProductForSelectDTO condition, Pageable pageable);

    Slice<ResponseProductHistoryDTO> getProductHistory(RequestProductHistoryDTO condition, Pageable pageable);

    void calcRentalAmount(List<RentalProductForCalculationDTO> calcProductRentalFeeList );

    @TransactionalEventListener(value = ProductStockEvent.class, phase = TransactionPhase.BEFORE_COMMIT)
    void expectStock(ProductStockEvent productStockEvent);

    List<ResponseProductStatusDTO> getProductStatus();
}
