package org.ateam.oncare.careproduct.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.RequestProductForSelectDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductDTO;
import org.ateam.oncare.careproduct.command.repository.ProductRepository;
import org.ateam.oncare.rental.command.dto.RentalHistoryDTO;
import org.ateam.oncare.rental.command.service.RentalService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final RentalService rentalService;

    @Override
    public Slice<ResponseProductDTO> getProduct(RequestProductForSelectDTO condition, Pageable pageable) {
        Slice<ResponseProductDTO> response = productRepository.getProduct(condition, pageable);

        List<String> productCodes = response.getContent().stream()
                .map(ResponseProductDTO::getId)
                .toList();

        log.debug("response.getContent()=[{}]", response.getContent());
        log.debug("productCodes:{}",productCodes);


        return null;
    }
}
