package org.ateam.oncare.beneficiary.query.controller;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.response.CareLevelOptionResponse;
import org.ateam.oncare.beneficiary.query.dto.response.MetaOptionResponse;
import org.ateam.oncare.beneficiary.query.service.BeneficiaryMetaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ✅ 프론트가 하드코딩 없이 옵션을 가져갈 수 있도록
 * "조회 전용" 메타 API 제공
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/beneficiaries/meta")
public class BeneficiaryMetaController {

    private final BeneficiaryMetaService metaService;

    /**
     * ✅ 태그 옵션
     * GET /api/beneficiaries/meta/tags
     */
    @GetMapping("/tags")
    public List<MetaOptionResponse> tags() {
        return metaService.getTagOptions();
    }

    /**
     * ✅ 위험요소 옵션
     * GET /api/beneficiaries/meta/risk-factors
     */
    @GetMapping("/risk-factors")
    public List<MetaOptionResponse> riskFactors() {
        return metaService.getRiskFactorOptions();
    }

    /**
     * ✅ 장기요양등급 옵션
     * GET /api/beneficiaries/meta/care-levels
     */
    @GetMapping("/care-levels")
    public List<CareLevelOptionResponse> careLevels() {
        return metaService.getCareLevelOptions();
    }
}
