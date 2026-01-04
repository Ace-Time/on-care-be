package org.ateam.oncare.rental.command.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.rental.command.dto.RentalContractForCalculationDTO;
import org.ateam.oncare.rental.command.entity.QRentalContract;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RentalContractRepositoryImpl implements RentalContractRepositoryCustom {
    private final JPAQueryFactory factory;
    private final QRentalContract rentalContract = QRentalContract.rentalContract;
    private final JdbcTemplate jdbcTemplate;

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

    @Override
    public void calculateContratMonthlyForRental(List<RentalContractForCalculationDTO> targetRentalContracts) {
        String sql = "update rental_contract " +
                     "set cumulative_revenue = ifnull(cumulative_revenue, 0) + ?" +
                     ", calc_date = ?" +
                     "where id = ? ";

        jdbcTemplate.batchUpdate(sql, targetRentalContracts, 1000,
                (PreparedStatement ps, RentalContractForCalculationDTO dto)->{
                    ps.setBigDecimal(1, BigDecimal.valueOf(dto.getCalculationAmount()));
                    ps.setObject(2,dto.getCalculationDate());
                    ps.setInt(3, dto.getId());
                });

    }
}
