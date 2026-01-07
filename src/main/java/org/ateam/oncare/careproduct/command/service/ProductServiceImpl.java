package org.ateam.oncare.careproduct.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.*;
import org.ateam.oncare.careproduct.command.entity.CareProduct;
import org.ateam.oncare.careproduct.command.entity.ProductStatus;
import org.ateam.oncare.careproduct.command.entity.ProductTask;
import org.ateam.oncare.careproduct.command.repository.ProductHistoryRepository;
import org.ateam.oncare.careproduct.command.repository.ProductRepository;
import org.ateam.oncare.careproduct.command.repository.ProductStatusRepository;
import org.ateam.oncare.careproduct.command.repository.ProductTaskRepository;
import org.ateam.oncare.careproduct.mapper.ProductStatusMapstruct;
import org.ateam.oncare.careproduct.mapper.ProductTaskMapStruct;
import org.ateam.oncare.global.enums.StockType;
import org.ateam.oncare.global.eventType.ProductStockEvent;
import org.ateam.oncare.rental.command.dto.RentalProductForCalculationDTO;
import org.ateam.oncare.rental.command.service.RentalService;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final RentalService rentalService;
    private final ProductHistoryRepository productHistoryRepository;
    private final ProductTaskRepository productTaskRepository;
    private final ProductTaskMapStruct productTaskMapStruct;
    private final ProductStatusRepository productStatusRepository;
    private final ProductStatusMapstruct  productStatusMapstruct;

    @Override
    public Slice<ResponseProductDTO> getProduct(RequestProductForSelectDTO condition, Pageable pageable) {
        Slice<ResponseProductDTO> response = productRepository.getProduct(condition, pageable);
        return response;
    }

    @Override
    public Slice<ResponseProductHistoryDTO> getProductHistory(RequestProductHistoryDTO condition, Pageable pageable) {
        Slice<ResponseProductHistoryDTO> result = productHistoryRepository.getProductHistories(condition, pageable);
        return result;
    }

    @Override
    public void calcRentalAmount(List<RentalProductForCalculationDTO> calcProductRentalFeeList) {
        productRepository.calculationRentalFee(calcProductRentalFeeList);

//        contractRepository.calculateContratMonthlyForRental(targetRentalContracts);
    }

    @Override
    public void expectStock(ProductStockEvent productStockEvent) {
        if (productStockEvent.getStatus() == StockType.OUTBOUND)
            outBound(productStockEvent);
        else if (productStockEvent.getStatus() == StockType.INBOUND)
            inBound(productStockEvent);
        else if(productStockEvent.getStatus() == StockType.Canceled)
            canceled(productStockEvent);
    }

    @Override
    public List<ResponseProductStatusDTO> getProductStatus() {

        List<ProductStatus> entities = productStatusRepository.findAll();
        List<ResponseProductStatusDTO> responseDTOs = entities.stream()
                .map(productStatusMapstruct::toDTO)
                .toList();

        return responseDTOs;
    }

    @Override
    public Slice<ResponsePlannedStockInOutDTO> getExpectedStock(RequestPlannedStockInOutDTO condition, Pageable pageable) {
        Slice<ResponsePlannedStockInOutDTO> entities = productTaskRepository.selectExpectedStock(condition, pageable);

        return entities;
    }

    private void canceled(ProductStockEvent productStockEvent) {
        ProductTask productTask = productTaskRepository.selectByProductId(productStockEvent.getProductId());
        productTaskRepository.delete(productTask);
    }

    private void outBound(ProductStockEvent productStockEvent) {
        List<CareProduct> entity = productRepository.findByProductCdAndProductStatus(
                productStockEvent.getProductCode(),
                1);

        if(entity.size() == 0)
            return;

        ProductTask productTask = productTaskMapStruct.toEntity(productStockEvent);
        productTask.setProductId(entity.get(0).getId());

        productTaskRepository.saveAndFlush(productTask);
    }

    private void inBound(ProductStockEvent productStockEvent) {
        CareProduct entity = productRepository.findById(productStockEvent.getProductId()).get();

        ProductTask productTask = productTaskMapStruct.toEntity(productStockEvent);
        productTask.setProductId(entity.getId());

        productTaskRepository.saveAndFlush(productTask);
    }

}
