package org.ateam.oncare.careproduct.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.careproduct.command.dto.RequestProductMasterDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseMasterCategoryDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductMasterDTO;
import org.ateam.oncare.careproduct.command.entity.ProductCategory;
import org.ateam.oncare.careproduct.command.repository.ProductCategoryRepository;
import org.ateam.oncare.careproduct.command.repository.ProductMasterRepository;
import org.ateam.oncare.careproduct.mapper.ProductCategoryMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductMasterServiceImpl implements ProductMasterService {

    private final ProductMasterRepository productMasterRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    @Override
    public Slice<ResponseProductMasterDTO> getProductMaster(RequestProductMasterDTO condition, Pageable pageable) {
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
}
