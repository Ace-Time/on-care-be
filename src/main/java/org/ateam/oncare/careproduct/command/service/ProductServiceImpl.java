package org.ateam.oncare.careproduct.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.RequestProductForSelectDTO;
import org.ateam.oncare.careproduct.command.dto.RequestProductHistoryDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductHistoryDTO;
import org.ateam.oncare.careproduct.command.repository.ProductHistoryRepository;
import org.ateam.oncare.careproduct.command.repository.ProductRepository;
import org.ateam.oncare.rental.command.service.RentalService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final RentalService rentalService;
    private final ProductHistoryRepository productHistoryRepository;

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
}
