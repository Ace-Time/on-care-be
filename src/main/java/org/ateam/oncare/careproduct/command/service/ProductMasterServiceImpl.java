package org.ateam.oncare.careproduct.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.*;
import org.ateam.oncare.careproduct.command.entity.CareProductMaster;
import org.ateam.oncare.careproduct.command.entity.ProductCategory;
import org.ateam.oncare.careproduct.command.repository.ProductCategoryRepository;
import org.ateam.oncare.careproduct.command.repository.ProductMasterRepository;
import org.ateam.oncare.careproduct.command.repository.ProductRepository;
import org.ateam.oncare.careproduct.mapper.ProductCategoryMapper;
import org.ateam.oncare.careproduct.mapper.ProductMasterMapper;
import org.ateam.oncare.config.customexception.ExistsAlreadyProductMasterException;
import org.ateam.oncare.rental.command.service.RentalService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductMasterServiceImpl implements ProductMasterService {

    private final ProductMasterRepository productMasterRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryMapper productCategoryMapper;
    private final ProductMasterMapper productMasterMapper;
    private final RentalService rentalService;


    @Override
    public Slice<ResponseProductMasterDTO> getProductMaster(RequestProductMasterForSelectDTO condition, Pageable pageable) {
        Slice<ResponseProductMasterDTO> response = productMasterRepository.selectProductMaster(condition, pageable);
        return response;
    }

    @Override
    @Cacheable(value = "masterData", key = "'product_category'")
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
    @CacheEvict(value = "masterData", key = "'product_category'")
    public int updateProductMaster(RequestProductMasterDTO requestProductMasterDTO) {
        CareProductMaster productMaster
                = productMasterRepository.findById(requestProductMasterDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 제품을 찾을 수 없습니다."));

        productMaster.setName(requestProductMasterDTO.getName());
        productMaster.setAmount(requestProductMasterDTO.getAmount());
        productMaster.setRentalAmount(requestProductMasterDTO.getRentalAmount());
        productMaster.setCategoryCd(requestProductMasterDTO.getCategoryCd());
        productMaster.setUpdatedAt(LocalDateTime.now());
        productMaster.setExplanation(requestProductMasterDTO.getExplanation());
        productMasterRepository.save(productMaster);

        return 1;
    }

    @Override
    public int registerProductMaster(RequestProductMasterDTO requestProductMasterDTO) {

        productMasterRepository.findById(requestProductMasterDTO.getId()).ifPresent(
                 entity -> {
                     throw new ExistsAlreadyProductMasterException(String.format("%s,이미 존재하는 아이템 입니다.", requestProductMasterDTO.getId()));
                 });

        CareProductMaster entity = productMasterMapper.toProductMasterEntity(requestProductMasterDTO);
        productMasterRepository.save(entity);

        return 1;
    }

    @Override
    public List<ProductAmountForRentalDTO> getProductAmountForRental(List<String> productCodes) {

        List<CareProductMaster> careProductMasters = productMasterRepository.findAllById(productCodes);

        List<ProductAmountForRentalDTO> productAmountForRentalDTOS
                = careProductMasters.stream()
                .map(e ->new ProductAmountForRentalDTO(
                    e.getId(),
                    e.getRentalAmount().intValue(),
                    e.getRentalAmount().divide(BigDecimal.valueOf(30),0, BigDecimal.ROUND_HALF_UP) //일률적으로 30일로 나눠서 하루치 렌탈료 구함
                            .setScale(-1 , RoundingMode.FLOOR).intValue() //1원 단위 절삭
                ))
                .toList();
        return productAmountForRentalDTOS;
    }

    /**
     * 관리용품 탭에 정보로 실 제품의 총재고, 가용, 렌탈 중인 데이터를 포함한 조회
     *
     * @param condition
     * @param pageable
     * @return
     */
    @Override
    public Slice<ResponseProductMasterDetailDTO> getProductMasterDetail(RequestProductMasterForSelectDTO condition, Pageable pageable) {
        // 1. 마스터 정보 조회
        Slice<ResponseProductMasterDTO> productMasters
                = productMasterRepository.selectProductMaster(condition, pageable);
        List<String> masterCodes =
                productMasters.stream()
                        .map(m -> m.getId())
                        .toList();

        // 2. 집계 데이터 조회(수량 정보)
        List<AggregationOfProductDTO> aggregationOfProductDTOS
                = productRepository.selectAggregationOfProduct(masterCodes);

        // 3. 출고 예정 데이터 조회(용품 계약만 한 상태)
        Map<String, Long> expectedToShip = rentalService.getExpectedToShip();

        // 4. 실 재고 수량에서 예정 출고 수량을 반영 해 가용재고 업데이트
        // 마스터 정보와 맵핑 할때 속도를 위해 map 으로 변환(key : productCode)
        Map<String, AggregationOfProductDTO> aggregationMap = aggregationOfProductDTOS.stream()
                .peek(data -> {
                    Long expectedCount = expectedToShip.getOrDefault(data.getProductCode(), 0L);

                    if (expectedCount > 0) {
                        data.setAvailable(data.getAvailable() - expectedCount.intValue());
                        data.setReserved(expectedCount.intValue());
                    }
                })
                .collect(Collectors.toMap(
                        AggregationOfProductDTO::getProductCode,
                        Function.identity()
                ));

        List<ResponseProductMasterDetailDTO> content = productMasters.getContent().stream()
                .map(master -> {
                    AggregationOfProductDTO agg = aggregationMap.get(master.getId());

                    int total = agg != null ? agg.getTotal() : 0;
                    int available = agg != null ? agg.getAvailable() : 0;
                    int rental = agg != null ? agg.getRental() : 0;
                    int discard = agg != null ? agg.getDiscard() : 0;
                    int reserved = agg != null ? agg.getReserved() : 0;

                    return new ResponseProductMasterDetailDTO(
                            master.getId(),
                            master.getName(),
                            master.getExplanation(),
                            master.getAmount(),
                            master.getRentalAmount(),
                            master.getCategoryCd(),
                            total,              // 총 수량
                            available,          // 가용 수량 (출고예정 차감 됨)
                            rental,             // 대여중 수량
                            discard,             // 폐기 수량
                            reserved,           // 출고 예정 수량
                            master.getAmount().intValue(),
                            master.getRentalAmount().intValue(),
                            master.getCategoryCd(),
                            master.getCategoryName()
                    );
                })
                .toList();

        return new SliceImpl<>(content, pageable ,productMasters.hasNext());
    }

}
