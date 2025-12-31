package org.ateam.oncare.careproduct.command.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.careproduct.command.entity.ProductTask;
import org.ateam.oncare.careproduct.command.entity.QProductTask;

@RequiredArgsConstructor
public class ProductTaskRepositoryImpl implements ProductTaskRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QProductTask productTask = QProductTask.productTask;

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
}
