package org.ateam.oncare.careproduct.command.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.*;
import org.ateam.oncare.careproduct.command.entity.CareProduct;
import org.ateam.oncare.careproduct.command.entity.QCareProduct;
import org.ateam.oncare.careproduct.mapper.ProductMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final ProductMapper productMapper;
    private final QCareProduct product = QCareProduct.careProduct;

    @Override
    public List<AggregationOfProductDTO> selectAggregationOfProduct(List<String> masterCodes) {
        List<AggregationOfProductDTO> aggregation =
                queryFactory
                        .select(
                                new QAggregationOfProductDTO(
                                        product.productCd,
                                        product.count().intValue(),
                                        new CaseBuilder().when(product.productStatus.notIn(2, 3, 4))
                                                .then(1)
                                                .otherwise(0)
                                                .sum(),
                                        product.productStatus.when(2).then(1).otherwise(0).sum(),
                                        Expressions.constant(0)
                                )
                        )
                        .from(product)
                        .where(product.productCd.in(masterCodes))
                        .groupBy(product.productCd)
                        .fetch();
        return aggregation;
    }

    @Override
    public Slice<ResponseProductDTO> getProduct(RequestProductForSelectDTO condition, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        int pageSize = pageable.getPageSize();

        // 제품 상태 값
        if (condition.getProductStatus() > 0)
            builder.and(product.productStatus.eq(condition.getProductStatus()));

        builder.and(product.productCd.eq(condition.getProductCode()));

        List<CareProduct> products = queryFactory
                .select(product)
                .from(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageSize + 1) //다음 데이터가 있는지 확인을 위해 1개 더 가지고옴
                .fetch();

        boolean hasNext = false;
        if(products.size() > pageSize) {
            hasNext = true;
            products.remove(pageSize);
        }

        List<ResponseProductDTO> dtos = products.stream()
                .map(productMapper::toProductDTO)
                .toList();

        return new SliceImpl<>(dtos, pageable, hasNext);
    }
}
