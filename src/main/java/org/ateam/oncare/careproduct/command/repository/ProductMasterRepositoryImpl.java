package org.ateam.oncare.careproduct.command.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.RequestProductMasterDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductMasterDTO;
import org.ateam.oncare.careproduct.command.entity.CareProductMaster;
import org.ateam.oncare.careproduct.command.entity.QCareProductMaster;
import org.ateam.oncare.careproduct.command.entity.QProductCategory;
import org.ateam.oncare.careproduct.mapper.ProductMasterMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ProductMasterRepositoryImpl implements ProductMasterRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QCareProductMaster master = QCareProductMaster.careProductMaster;
    private final QProductCategory category = QProductCategory.productCategory;
    private final ProductMasterMapper productMasterMapper;

    @Override
    public Slice<ResponseProductMasterDTO> selectProductMaster(RequestProductMasterDTO condition, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();
        BooleanBuilder s_builder = new BooleanBuilder();
        int pageSize = pageable.getPageSize();

        //카테고리 name
        if(StringUtils.hasText(condition.getCategoryName()) &&
        !condition.getCategoryName().equals("전체"))
            builder.and(category.name.eq(condition.getCategoryName()));

        //제품명
        if(StringUtils.hasText(condition.getCodeOrName()))
            s_builder.or(master.name.containsIgnoreCase(condition.getCodeOrName()));

        //제품코드
        if(StringUtils.hasText(condition.getCodeOrName()))
            s_builder.or(master.id.containsIgnoreCase(condition.getCodeOrName()));

        if(s_builder.hasValue())
            builder.and(s_builder);

        List<CareProductMaster> entitys =
                queryFactory
                        .select(master)
                        .from(master)
                        .leftJoin(category).on(master.categoryCd.eq(category.id))
                        .where(builder)
                        .offset(pageable.getOffset())
                        .limit(pageSize + 1) //다음 데이터가 있는지 확인을 위해 1개 더 가지고옴
                        .fetch();

        boolean hasNext = false;
        if(entitys.size() > pageSize) {
            hasNext = true;
            entitys.remove(pageSize);   // 뒤에 데이터가 있는지 확인을 위해 1개 더 가지고온 데이터 삭제
        }

        List<ResponseProductMasterDTO> dtos
                = entitys.stream()
                .map(m -> productMasterMapper.toProductMasterDTO(m))
                .toList();

        return new SliceImpl<>(dtos, pageable, hasNext);
    }
}
