package org.ateam.oncare.customer.query.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.beneficiary.query.dto.response.PageResponse;
import org.ateam.oncare.customer.query.dto.CustomerManageDTO;
import org.ateam.oncare.customer.query.service.CustomerManageQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/customer-manage")
@RequiredArgsConstructor
public class CustomerManageQueryController {

    private final CustomerManageQueryService customerManageQueryService;

    /**
     * 수급자 목록 조회 (카테고리 필터링 포함)
     * GET /api/customer-manage/beneficiaries?keyword=&category=&subCategory=&page=0&size=10
     */
    @GetMapping("/beneficiaries")
    public ResponseEntity<PageResponse<CustomerManageDTO.BeneficiaryListItem>> getBeneficiaryList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String subCategory,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        CustomerManageDTO.SearchCondition condition = CustomerManageDTO.SearchCondition.builder()
                .keyword(keyword)
                .category(category)
                .subCategory(subCategory)
                .page(page)
                .size(size)
                .build();

        log.info("수급자 목록 조회 - keyword: {}, category: {}, subCategory: {}", keyword, category, subCategory);

        PageResponse<CustomerManageDTO.BeneficiaryListItem> result = customerManageQueryService.getBeneficiaryList(condition);
        return ResponseEntity.ok(result);
    }

    /**
     * 카테고리별 카운트 조회
     * GET /api/customer-manage/category-counts
     */
    @GetMapping("/category-counts")
    public ResponseEntity<CustomerManageDTO.CategoryCount> getCategoryCounts() {
        CustomerManageDTO.CategoryCount counts = customerManageQueryService.getCategoryCounts();
        return ResponseEntity.ok(counts);
    }

    /**
     * 고객 관리 상세 조회 (5단계용)
     * GET /api/customer-manage/beneficiaries/{beneficiaryId}/detail
     */
    @GetMapping("/beneficiaries/{beneficiaryId}/detail")
    public ResponseEntity<CustomerManageDTO.CustomerManageDetail> getCustomerManageDetail(
            @PathVariable Long beneficiaryId
    ) {
        log.info("고객 관리 상세 조회 - beneficiaryId: {}", beneficiaryId);
        CustomerManageDTO.CustomerManageDetail detail = customerManageQueryService.getCustomerManageDetail(beneficiaryId);
        return ResponseEntity.ok(detail);
    }
}
