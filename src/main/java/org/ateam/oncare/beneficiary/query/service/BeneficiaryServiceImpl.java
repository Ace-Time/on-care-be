package org.ateam.oncare.beneficiary.query.service;


import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.request.BeneficiarySearchRequest;
import org.ateam.oncare.beneficiary.query.dto.response.BeneficiaryListItemResponse;
import org.ateam.oncare.beneficiary.query.dto.response.PageResponse;
import org.ateam.oncare.beneficiary.query.mapper.BeneficiaryMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiaryMapper beneficiaryMapper;

    @Override
    public PageResponse<BeneficiaryListItemResponse> getBeneficiaries(BeneficiarySearchRequest req) {

        if ("ALL".equalsIgnoreCase(req.getStatus())) {
            req.setStatus(null);
        }

        normalize(req);

        long total = beneficiaryMapper.countBeneficiaries(req);
        List<BeneficiaryListItemResponse> list =
                beneficiaryMapper.selectBeneficiaries(req);

        int totalPages = (int) Math.ceil((double) total / req.getSize());

        return PageResponse.<BeneficiaryListItemResponse>builder()
                .content(list)
                .page(req.getPage())
                .size(req.getSize())
                .totalElements(total)
                .totalPages(totalPages)
                .build();
    }

    private void normalize(BeneficiarySearchRequest req) {
        if (!StringUtils.hasText(req.getSort())) req.setSort("NAME");
        if (!StringUtils.hasText(req.getDirection())) req.setDirection("ASC");

        req.setSort(req.getSort().toUpperCase());
        req.setDirection(req.getDirection().toUpperCase());

        if (!req.getDirection().equals("DESC")) {
            req.setDirection("ASC");
        }

        // ✅ keyword 공백 제거 + 빈문자열이면 null 처리 (XML if문 안전)
        if (req.getKeyword() != null) {
            String trimmed = req.getKeyword().trim();
            req.setKeyword(trimmed.isEmpty() ? null : trimmed);
        }
    }
}