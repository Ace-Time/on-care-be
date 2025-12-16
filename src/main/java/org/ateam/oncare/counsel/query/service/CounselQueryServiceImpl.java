package org.ateam.oncare.counsel.query.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.counsel.query.dto.CounselDetailResponse;
import org.ateam.oncare.counsel.query.dto.CounselListResponse;
import org.ateam.oncare.counsel.query.dto.CustomerListResponse;
import org.ateam.oncare.counsel.query.mapper.CounselQueryMapper;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CounselQueryServiceImpl implements CounselQueryService {
    private final CounselQueryMapper counselQueryMapper;

    @Override
    public @Nullable List<CustomerListResponse> searchCustomers(String keyword) {
        if(!StringUtils.hasText(keyword)){
            return counselQueryMapper.findAllCustomers();
        }
        if (keyword.matches("^[0-9-]*$")) {
            String cleanPhone = keyword.replaceAll("-", "");
            return counselQueryMapper.searchCustomersByPhone(cleanPhone);
        }
        return counselQueryMapper.searchCustomersByName(keyword);
    }

    @Override
    public @Nullable Slice<CounselListResponse> findCounselsByCustomerId(BigInteger customerId, String customerType,
                                                                         String counselCategoryName, Pageable pageable) {
        int pageSize = pageable.getPageSize(); // 5
        int limit = pageSize + 1;              // 6 (검증용)
        long offset = pageable.getOffset();

        // DB 조회
        List<CounselListResponse> counselList = counselQueryMapper.findCounselsByCustomerId(customerId, customerType,
                counselCategoryName, limit, offset);

        boolean hasNext = false;             // 다음 페이지가 없으면 스크롤 안되도록
        if (counselList.size() > pageSize) {
            hasNext = true;                  // 6개가 조회되었다면, 다음 페이지가 있다는 뜻
            counselList.remove(pageSize);    // 사용자에게는 요청한 5개만 줘야 하므로, 마지막 1개(검증용)는 리스트에서 제거
        }
        return new SliceImpl<>(counselList, pageable, hasNext);
    }

    @Override
    public @Nullable CounselDetailResponse findCounselDetailById(BigInteger counselHistoryId) {
        return counselQueryMapper.findCounselDetailById(counselHistoryId);
    }
}
