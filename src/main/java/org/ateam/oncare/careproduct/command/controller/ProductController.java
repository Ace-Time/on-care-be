package org.ateam.oncare.careproduct.command.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.RequestProductMasterDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseMasterCategoryDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductMasterDTO;
import org.ateam.oncare.careproduct.command.service.ProductMasterService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductMasterService productMasterService;

    @GetMapping("/master")
    public ResponseEntity<Slice<ResponseProductMasterDTO>> getProductMaster(
            RequestProductMasterDTO condition,
            @PageableDefault(size = 10) Pageable pageable){

        Slice<ResponseProductMasterDTO> response
                = productMasterService.getProductMaster(condition, pageable);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/master-category")
    public ResponseEntity<List<ResponseMasterCategoryDTO>> getMasterCategory() {
        List<ResponseMasterCategoryDTO> response = productMasterService.getMasterCategory();
        return ResponseEntity.ok()
                .body(response);
    }
}
