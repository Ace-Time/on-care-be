package org.ateam.oncare.customer.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.beneficiary.query.dto.response.PageResponse;
import org.ateam.oncare.customer.query.dto.*;
import org.ateam.oncare.customer.query.dto.CustomerManageDTO.BeneficiaryListItem;
import org.ateam.oncare.customer.query.mapper.CustomerManageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerManageQueryService {

    private final CustomerManageMapper customerManageMapper;

    /**
     * 수급자 목록 조회 (카테고리 필터링 포함)
     */
    public PageResponse<BeneficiaryListItem> getBeneficiaryList(CustomerManageDTO.SearchCondition condition) {
        // 기본값 설정
        if (condition.getPage() == null) condition.setPage(0);
        if (condition.getSize() == null) condition.setSize(10);

        condition.setOffset(condition.getPage() * condition.getSize());

        List<BeneficiaryListItem> content = customerManageMapper.selectBeneficiaryList(condition);
        long totalElements = customerManageMapper.countBeneficiaryList(condition);
        int totalPages = (int) Math.ceil((double) totalElements / condition.getSize());

        return PageResponse.<BeneficiaryListItem>builder()
                .content(content)
                .page(condition.getPage())
                .size(condition.getSize())
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }

    /**
     * 카테고리별 카운트 조회
     */
    public CustomerManageDTO.CategoryCount getCategoryCounts() {
        return customerManageMapper.selectCategoryCounts();
    }

    /**
     * 고객 관리 상세 조회 (5단계용)
     */
    public CustomerManageDTO.CustomerManageDetail getCustomerManageDetail(Long beneficiaryId) {
        CustomerManageDTO.CustomerManageDetail detail = customerManageMapper.selectCustomerManageDetail(beneficiaryId);

        if (detail == null) {
            throw new IllegalArgumentException("수급자를 찾을 수 없습니다: " + beneficiaryId);
        }

        // 불만상담 정보 조회
        if (Boolean.TRUE.equals(detail.getHasComplaint())) {
            CustomerManageDTO.CounselSummary latestComplaint = customerManageMapper.selectLatestComplaint(beneficiaryId);
            detail.setLatestComplaint(latestComplaint);
        }

        // 해지상담 정보 조회
        if (Boolean.TRUE.equals(detail.getHasTermination())) {
            CustomerManageDTO.CounselSummary latestTermination = customerManageMapper.selectLatestTermination(beneficiaryId);
            detail.setLatestTermination(latestTermination);
        }

        // 렌탈상담 정보 조회
        if (Boolean.TRUE.equals(detail.getHasRentalCounsel())) {
            CustomerManageDTO.CounselSummary latestRentalCounsel = customerManageMapper.selectLatestRentalCounsel(beneficiaryId);
            detail.setLatestRentalCounsel(latestRentalCounsel);
        }

        return detail;
    }

    /**
     * 담당 요양보호사 ID 조회
     */
    public Integer getCareWorkerId(Long beneficiaryId) {
        return customerManageMapper.selectCareWorkerId(beneficiaryId);
    }
}

