package org.ateam.oncare.beneficiary.query.controller;

import org.ateam.oncare.beneficiary.query.dto.request.BeneficiarySearchRequest;
import org.ateam.oncare.beneficiary.query.dto.response.BeneficiaryListItemResponse;
import org.ateam.oncare.beneficiary.query.dto.response.PageResponse;
import org.ateam.oncare.beneficiary.query.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private static final Set<Integer> ALLOWED_SIZES = Set.of(10, 15, 20, 30);

    private final BeneficiaryService beneficiaryService;

    @GetMapping
    public PageResponse<BeneficiaryListItemResponse> getBeneficiaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer riskLevelId,
            @RequestParam(required = false) Integer careLevelId,

            // ✅ 추가
            @RequestParam(required = false) String keyword,

            @RequestParam(defaultValue = "NAME") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        if (!ALLOWED_SIZES.contains(size)) size = 10;

        BeneficiarySearchRequest req = new BeneficiarySearchRequest();
        req.setPage(Math.max(page, 0));
        req.setSize(size);
        req.setStatus(status);
        req.setRiskLevelId(riskLevelId);
        req.setCareLevelId(careLevelId);

        // ✅ 추가
        req.setKeyword(keyword);

        req.setSort(sort);
        req.setDirection(direction);

        return beneficiaryService.getBeneficiaries(req);
    }
}