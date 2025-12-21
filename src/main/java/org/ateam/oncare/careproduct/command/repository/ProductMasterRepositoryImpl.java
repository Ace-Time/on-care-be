package org.ateam.oncare.careproduct.command.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.RequestProductMasterForSelectDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductMasterDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductMasterDetailDTO;
import org.ateam.oncare.careproduct.command.entity.CareProductMaster;
import org.ateam.oncare.careproduct.command.entity.QCareProduct;
import org.ateam.oncare.careproduct.command.entity.QCareProductMaster;
import org.ateam.oncare.careproduct.command.entity.QProductCategory;
import org.ateam.oncare.careproduct.mapper.ProductMasterMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ProductMasterRepositoryImpl implements ProductMasterRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QCareProductMaster master = QCareProductMaster.careProductMaster;
    private final QProductCategory category = QProductCategory.productCategory;
    private final ProductMasterMapper productMasterMapper;
    private final QCareProduct product = QCareProduct.careProduct;

    @Override
    public Slice<ResponseProductMasterDTO> selectProductMaster(RequestProductMasterForSelectDTO condition, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        int pageSize = pageable.getPageSize();

        //카테고리 name
        if (StringUtils.hasText(condition.getCategoryCode()))
            builder.and(category.id.eq(condition.getCategoryCode()));

        //제품명 및 제품코드
        if (StringUtils.hasText(condition.getCodeOrName())) {
            builder.and(master.name.containsIgnoreCase(condition.getCodeOrName())
                    .or(master.id.containsIgnoreCase(condition.getCodeOrName())));
        }

        List<CareProductMaster> entities =
                queryFactory
                        .select(master)
                        .from(master)
                        .leftJoin(category).on(master.categoryCd.eq(category.id))
                        .where(builder)
                        .offset(pageable.getOffset())
                        .limit(pageSize + 1) //다음 데이터가 있는지 확인을 위해 1개 더 가지고옴
                        .fetch();

        boolean hasNext = false;
        if (entities.size() > pageSize) {
            hasNext = true;
            entities.remove(pageSize);   // 뒤에 데이터가 있는지 확인을 위해 1개 더 가지고온 데이터 삭제
        }

        List<ResponseProductMasterDTO> dtos
                = entities.stream()
                .map(productMasterMapper::toProductMasterDTO)
                .toList();

        return new SliceImpl<>(dtos, pageable, hasNext);
    }

    @Override
    public Slice<ResponseProductMasterDetailDTO> selectProductMasterDetail(RequestProductMasterForSelectDTO condition, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        int pageSize = pageable.getPageSize();

        //카테고리 name
        if (StringUtils.hasText(condition.getCategoryCode()) &&
                !condition.getCategoryCode().equals("전체"))
            builder.and(category.name.eq(condition.getCategoryCode()));

        //제품명 및 제품코드
        if (StringUtils.hasText(condition.getCodeOrName())) {
            builder.and(master.name.containsIgnoreCase(condition.getCodeOrName())
                    .or(master.id.containsIgnoreCase(condition.getCodeOrName())));
        }

//        List<ResponseProductMasterDetailDTO> results =
//                queryFactory
//                        .select(
//                                master,
//                                JPAExpressions
//                                        .select()
//                                        .from(product)
//                                        .groupBy(product.productCd)
//                                )
//                        .from(master)
//                        .leftJoin(category).on(master.categoryCd.eq(category.id))
//                        .where(builder)
//                        .fetch();
        return null;
    }
}
