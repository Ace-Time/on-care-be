package org.ateam.oncare.careproduct.command.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.*;
import org.ateam.oncare.careproduct.command.entity.CareProduct;
import org.ateam.oncare.careproduct.command.entity.QCareProduct;
import org.ateam.oncare.careproduct.command.entity.QProductStatus;
import org.ateam.oncare.careproduct.mapper.ProductMapper;
import org.ateam.oncare.rental.command.dto.RentalContractForCalculationDTO;
import org.ateam.oncare.rental.command.dto.RentalProductForCalculationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final ProductMapper productMapper;
    private final QCareProduct product = QCareProduct.careProduct;
    private final QProductStatus productStatus = QProductStatus.productStatus;
    private final JdbcTemplate jdbcTemplate;

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
                                        product.productStatus.when(3).then(1).otherwise(0).sum(),
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

        List<Tuple> products = queryFactory
                .select(product,productStatus.name)
                .from(product)
                .join(productStatus).on(productStatus.id.eq(product.productStatus.longValue()))
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
                .map(data ->{
                    ResponseProductDTO dto = productMapper.toProductDTO(data.get(product));
                    dto.setStatusName(String.valueOf(data.get(productStatus.name)));
                    return dto;
                })
                .toList();

        return new SliceImpl<>(dtos, pageable, hasNext);
    }

    @Override
    public void calculationRentalFee(List<RentalProductForCalculationDTO> calcProductRentalFeeList) {
        String sql = "update care_product " +
                "set cumulative_revenue = ifnull(cumulative_revenue, 0) + ? " +
                "where id = ? ";

        jdbcTemplate.batchUpdate(sql, calcProductRentalFeeList, 1000,
                (PreparedStatement ps, RentalProductForCalculationDTO dto)->{
                    ps.setBigDecimal(1, BigDecimal.valueOf(dto.getCalcAmount()));
                    ps.setString(2, dto.getProductCode());
                });

    }
}
