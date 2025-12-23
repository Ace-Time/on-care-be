package org.ateam.oncare.careproduct.command.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.RequestProductHistoryDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductHistoryDTO;
import org.ateam.oncare.careproduct.command.entity.ProductHistory;
import org.ateam.oncare.careproduct.command.entity.QProductHistory;
import org.ateam.oncare.careproduct.mapper.ProductHistoryMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ProductHistoryRepositoryImpl implements ProductHistoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QProductHistory productHistory = QProductHistory.productHistory;
    private final ProductHistoryMapper productHistoryMapper;

    @Override
    public Slice<ResponseProductHistoryDTO> getProductHistories(RequestProductHistoryDTO condition, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        int pageSize = pageable.getPageSize();

        if (StringUtils.hasText(condition.getHistoryStatus()))
            builder.and(productHistory.status.stringValue().eq(condition.getHistoryStatus()));

        builder.and(productHistory.productId.eq(condition.getProductId()));

        List<ProductHistory> productHistories = queryFactory
                .select(productHistory)
                .from(productHistory)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = false;

        if(productHistories.size() > pageSize) {
            hasNext = true;
            productHistories.remove(pageSize);
        }

        productHistories.forEach(m -> log.debug(m.toString()));

        List<ResponseProductHistoryDTO> responseDTOs =
                productHistories.stream()
                        .map(productHistoryMapper::toHistoryDTO)
                        .toList();

        return new SliceImpl<>(responseDTOs, pageable, hasNext);
    }
}
