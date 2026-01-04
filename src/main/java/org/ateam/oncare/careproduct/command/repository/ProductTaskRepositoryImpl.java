package org.ateam.oncare.careproduct.command.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.careproduct.command.dto.RequestPlannedStockInOutDTO;
import org.ateam.oncare.careproduct.command.dto.ResponsePlannedStockInOutDTO;
import org.ateam.oncare.careproduct.command.entity.ProductTask;
import org.ateam.oncare.careproduct.command.entity.QCareProduct;
import org.ateam.oncare.careproduct.command.entity.QCareProductMaster;
import org.ateam.oncare.careproduct.command.entity.QProductTask;
import org.ateam.oncare.careproduct.command.enums.ProductStockStatus;
import org.ateam.oncare.careproduct.mapper.ProductTaskMapStruct;
import org.ateam.oncare.employee.command.entity.QEmployee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ProductTaskRepositoryImpl implements ProductTaskRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QProductTask productTask = QProductTask.productTask;
    private final QCareProductMaster productMaster = QCareProductMaster.careProductMaster;
    private final QCareProduct careProduct = QCareProduct.careProduct;
    private final ProductTaskMapStruct productTaskMapStruct;
    private final QEmployee employee = QEmployee.employee;

    @Override
    public ProductTask selectByProductId(String productId) {
        ProductTask findTask = queryFactory.select(productTask)
                .from(productTask)
                .where(productTask.productId.eq(productId)
                        .and(productTask.isConfirmed.matches("N")))
                .orderBy(productTask.createdAt.desc())
                .fetchOne();

        return findTask;
    }

    @Override
    public Slice<ResponsePlannedStockInOutDTO> selectExpectedStock(RequestPlannedStockInOutDTO condition, Pageable pageable) {
        int pageSize = pageable.getPageSize();

        List<Tuple> findTasks = queryFactory
                .select(productTask, productMaster.name, employee.name)
                .from(productTask)
                .join(careProduct).on(careProduct.id.eq(productTask.productId))
                .join(productMaster).on(productMaster.id.eq(careProduct.productCd))
                .leftJoin(employee).on(employee.id.eq(productTask.employeeId))
                .where(idEq(condition.getId()),
                        productNameOrProductIdContain(condition.getProductNameOrProductCode()),
//                        productIdEq(condition.getProductId()),
                        expectedDate(condition.getExpectedStartDate(), condition.getExpectedEndDate()),
                        statusEq(condition.getStatus())
                )
                .limit(pageSize + 1)
                .offset(pageable.getOffset())
                .fetch();

        boolean hasNext = false;


        if (findTasks.size() > pageSize) {
            hasNext = true;
            findTasks.remove(pageSize);
        }

        List<ResponsePlannedStockInOutDTO> planned =
                findTasks.stream()
                        .map(e -> {
                            ResponsePlannedStockInOutDTO dto
                                    = productTaskMapStruct.toDTO(e.get(productTask));

                            dto.setStatusName(ProductStockStatus.values()[dto.getStatus()].name());
                            dto.setProductName(e.get(productMaster.name));
                            dto.setEmployeeName(e.get(employee.name));
                            return dto;
                        })
                        .toList();


        return new SliceImpl<>(planned, pageable, hasNext);
    }


    private BooleanExpression idEq(Long id) {
        return id != null ? productTask.id.eq(id) : null;

    }

    private BooleanExpression statusEq(Integer status) {
        return status != null ? productTask.status.eq(status) : null;
    }

    private BooleanExpression productNameOrProductIdContain(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }

        // 두 조건을 or로 결합하여 하나의 BooleanExpression으로 반환
        return productMaster.name.containsIgnoreCase(name)
                .or(productTask.productId.containsIgnoreCase(name));
    }

    private BooleanExpression productIdEq(String productId) {
        return productId == null || productId.trim().isEmpty() ? null : productTask.productId.eq(productId);
    }

    private BooleanExpression expectedDate(LocalDate startDate, LocalDate endDate) {
        if (startDate != null  && endDate == null) {
            return productTask.expectedDate.goe(startDate);
        } else if (startDate == null && endDate == null) {
            return null;
        }

        return productTask.expectedDate.goe(startDate)
                .and(productTask.expectedDate.loe(endDate));
    }
}
