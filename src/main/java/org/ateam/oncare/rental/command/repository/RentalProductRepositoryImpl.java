package org.ateam.oncare.rental.command.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.rental.command.dto.RentalProductForCalculationDTO;
import org.ateam.oncare.rental.command.entity.QRentalProduct;
import org.ateam.oncare.rental.command.entity.RentalProduct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class RentalProductRepositoryImpl implements RentalProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QRentalProduct rentalProduct = QRentalProduct.rentalProduct;

    @Override
    public Map<Long,List<RentalProductForCalculationDTO>> selectRentalProduct(List<Long> contratIdList) {
        List<RentalProduct> list = queryFactory
                .select(rentalProduct)
                .from(rentalProduct)
                .where(rentalProduct.rentalContractCd.in(contratIdList))
                .fetch();

        Map<Long,List<RentalProductForCalculationDTO>> dataMaps =  list.stream()
                .collect(Collectors.groupingBy(
                        RentalProduct::getRentalContractCd
                        ,Collectors.mapping(entity ->{
                            return RentalProductForCalculationDTO.builder()
                                    .productCode(entity.getProductId())
                                    .startDate(entity.getStartDate().toLocalDate())
                                    .endDate(Optional.ofNullable(entity.getEndDate())
                                                    .map(LocalDateTime::toLocalDate)
                                                    .orElse(null)
                                            )
                                    .build();
                        }, Collectors.toList())
                ));

        return dataMaps;
    }
}
