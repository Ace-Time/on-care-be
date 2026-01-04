package org.ateam.oncare.beneficiary.query.controller;

import io.jsonwebtoken.Claims;
import org.ateam.oncare.auth.security.JwtTokenProvider;
import org.ateam.oncare.beneficiary.query.dto.request.BeneficiarySearchRequest;
import org.ateam.oncare.beneficiary.query.dto.response.BeneficiaryListItemResponse;
import org.ateam.oncare.beneficiary.query.dto.response.PageResponse;
import org.ateam.oncare.beneficiary.query.mapper.BeneficiaryMapper;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final BeneficiaryMapper beneficiaryMapper;

    @GetMapping
    public PageResponse<BeneficiaryListItemResponse> getBeneficiaries(
            @RequestHeader(value = "Authorization", required = false) String authHeader,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer riskLevelId,
            @RequestParam(required = false) Integer careLevelId,


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


        req.setKeyword(keyword);

        req.setSort(sort);
        req.setDirection(direction);

        //  요양보호사 필터링: JWT 토큰에서 employeeId를 가져와서 careWorkerId 조회
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = jwtTokenProvider.getClaimsFromAT(token);
            Long employeeId = claims.get("id", Long.class);

            // employeeId로 careWorkerId 조회 (요양보호사인 경우에만 값이 존재)
            Long careWorkerId = beneficiaryMapper.selectCareWorkerIdByEmployeeId(employeeId);

            // careWorkerId가 있으면 (요양보호사이면) 필터 설정
            if (careWorkerId != null) {
                req.setCareWorkerId(careWorkerId);
            }
        }

        return beneficiaryService.getBeneficiaries(req);
    }
}