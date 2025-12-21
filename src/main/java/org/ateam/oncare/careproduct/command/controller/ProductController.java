package org.ateam.oncare.careproduct.command.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.*;
import org.ateam.oncare.careproduct.command.service.ProductMasterService;
import org.ateam.oncare.careproduct.command.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductMasterService productMasterService;
    private final ProductService productService;

    @GetMapping("/master-category")
    public ResponseEntity<List<ResponseMasterCategoryDTO>> getMasterCategory() {
        List<ResponseMasterCategoryDTO> response = productMasterService.getMasterCategory();
        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/master")
    public ResponseEntity<Slice<ResponseProductMasterDTO>> getProductMaster(
            RequestProductMasterForSelectDTO condition,
            @PageableDefault(size = 10) Pageable pageable) {

        Slice<ResponseProductMasterDTO> response
                = productMasterService.getProductMaster(condition, pageable);

        return ResponseEntity.ok()
                .body(response);
    }


    /**
     * 관리용품 탭에 정보로 실 제품의 총재고, 가용, 렌탈 중인 데이터를 포함한 조회
     *
     * @param condition
     * @param pageable
     * @return
     */
    @GetMapping("/master-detail")
    public ResponseEntity<Slice<ResponseProductMasterDetailDTO>> getProductMasterDetail(
            RequestProductMasterForSelectDTO condition,
            @PageableDefault(size = 10) Pageable pageable) {

        Slice<ResponseProductMasterDetailDTO> response
                = productMasterService.getProductMasterDetail(condition, pageable);

        return ResponseEntity.ok()
                .body(response);
    }


    @PatchMapping("/master")
    public ResponseEntity<Integer> updateProductMaster(@RequestBody RequestProductMasterDTO requestProductMasterDTO) {
        int count = productMasterService.updateProductMaster(requestProductMasterDTO);

        return ResponseEntity.ok(count);
    }

    @GetMapping("/product")
    public ResponseEntity<Slice<ResponseProductDTO>> getProduct(
            RequestProductForSelectDTO condition,
            @PageableDefault(size = 10) Pageable pageable) {

        Slice<ResponseProductDTO> response =
                productService.getProduct(condition,pageable );
        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/product-history")
    public ResponseEntity<Slice<ResponseProductHistroyDTO>> getProductHistory(
            RequestProductHistoryDTO condition,
            @PageableDefault(size = 10) Pageable pageable) {

        Slice<ResponseProductHistroyDTO> response =
                productService.getProductHistory(condition,pageable);

        return ResponseEntity.ok()
                .body(response);

    }


}
