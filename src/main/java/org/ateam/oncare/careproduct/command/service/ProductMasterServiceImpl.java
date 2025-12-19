package org.ateam.oncare.careproduct.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.RequestProductMasterDTO;
import org.ateam.oncare.careproduct.command.dto.RequestProductMasterForSelectDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseMasterCategoryDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductMasterDTO;
import org.ateam.oncare.careproduct.command.entity.CareProductMaster;
import org.ateam.oncare.careproduct.command.entity.ProductCategory;
import org.ateam.oncare.careproduct.command.repository.ProductCategoryRepository;
import org.ateam.oncare.careproduct.command.repository.ProductMasterRepository;
import org.ateam.oncare.careproduct.mapper.ProductCategoryMapper;
import org.ateam.oncare.careproduct.mapper.ProductMasterMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductMasterServiceImpl implements ProductMasterService {

    private final ProductMasterRepository productMasterRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;
    private final ProductMasterMapper productMasterMapper;

    @Override
    public Slice<ResponseProductMasterDTO> getProductMaster(RequestProductMasterForSelectDTO condition, Pageable pageable) {
        Slice<ResponseProductMasterDTO> response = productMasterRepository.selectProductMaster(condition,pageable);
        return response;
    }

    @Override
    public List<ResponseMasterCategoryDTO> getMasterCategory() {
        List<ProductCategory> categories
                = productCategoryRepository.findAll();

        List<ResponseMasterCategoryDTO> response =
                categories.stream()
                        .map(productCategoryMapper::toCategoryDTO)
                        .toList();
        return response;
    }

    @Override
    public int updateProductMaster(RequestProductMasterDTO requestProductMasterDTO) {
        log.debug("requestProductMasterDTO:{}",requestProductMasterDTO);
        CareProductMaster productMaster
                = productMasterRepository.findById(requestProductMasterDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 제품을 찾을 수 없습니다."));
        productMaster.setName(requestProductMasterDTO.getName());
        productMaster.setAmount(requestProductMasterDTO.getAmount());
        productMaster.setRentalAmount(requestProductMasterDTO.getRentalAmount());
        productMaster.setUpdatedAt(LocalDateTime.now());
        productMasterRepository.save(productMaster);

        return 1;
    }
}
