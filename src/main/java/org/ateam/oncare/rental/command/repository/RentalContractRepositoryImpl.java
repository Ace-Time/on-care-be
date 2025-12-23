package org.ateam.oncare.rental.command.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.rental.command.entity.QRentalContract;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RentalContractRepositoryImpl implements RentalContractRepositoryCustom {
    private final JPAQueryFactory factory;
    private final QRentalContract rentalContract = QRentalContract.rentalContract;

    @Override
    public Map<String, Long> selectExpectedToShip() {
        List<Tuple> result = factory
                .select(rentalContract.productCd, rentalContract.count())
                .from(rentalContract)
                .where(rentalContract.contractStatusCd.eq(1L))
                .groupBy(rentalContract.productCd)
                .fetch();

        Map<String, Long> response =
                result.stream()
                        .collect(Collectors.toMap(
                                tuple -> tuple.get(rentalContract.productCd),
                                tuple -> tuple.get(rentalContract.count())
                        ));

        return response;
    }
}
