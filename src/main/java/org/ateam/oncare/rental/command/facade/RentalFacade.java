package org.ateam.oncare.rental.command.facade;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.ProductAmountForRentalDTO;
import org.ateam.oncare.careproduct.command.service.ProductMasterService;
import org.ateam.oncare.careproduct.command.service.ProductService;
import org.ateam.oncare.config.customexception.NotFoundProductMasterException;
import org.ateam.oncare.rental.command.dto.RentalContractForCalculationDTO;
import org.ateam.oncare.rental.command.dto.RentalProductForCalculationDTO;
import org.ateam.oncare.rental.command.entity.RentalProduct;
import org.ateam.oncare.rental.command.service.RentalService;
import org.ateam.oncare.rental.query.service.RentalQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalFacade {
    private final RentalService rentalService;
    private final ProductMasterService productMasterService;
    private final RentalQueryService rentalQueryService;
    private final ProductService productService;
    private final TransactionTemplate transactionTemplate;


    public int calcRentalAmount(LocalDate calcDate) {
        //정산 할 렌탈 계약 목록 조회
        List<RentalContractForCalculationDTO> targetRentalContracts =
                rentalQueryService.getTargetRentalContracts(calcDate);

        if (targetRentalContracts.size() == 0)
            return 0;

        //제품코드만 리스트로 집계
        List<String> productCodes = targetRentalContracts.stream()
                .map(RentalContractForCalculationDTO::getProductCd)
                .distinct()
                .toList();


        //제품 코드별 단가 조회(월단위, 일단위)
        List<ProductAmountForRentalDTO> productAmountForRentalDTOS
                = productMasterService.getProductAmountForRental(productCodes);

        // 검색을 위해 productCODE를 key값으로 하는 Map 생성
        Map<String, ProductAmountForRentalDTO> productAmountForRentalMap =
                productAmountForRentalDTOS.stream()
                        .collect(Collectors.toMap(
                                ProductAmountForRentalDTO::getProductCode,
                                p -> p
                        ));

        log.debug("productAmountForRentalDTOS:{}", productAmountForRentalDTOS);
        // 제품 마스터가 등록되지 않은 렌탈 계약건이 1건이라도 있을 경우 에러 처리
        targetRentalContracts.stream()
                .filter(x -> !productAmountForRentalMap.containsKey(x.getProductCd()))
                .findFirst()
                .ifPresent(x -> {
                    throw new NotFoundProductMasterException("렌탈 장비 정산 작업중 " + x.getProductCd() + " 제품의 마스터 정보를 찾지 못 함");
                });

        //정산월 사용 금액 계산
        targetRentalContracts.stream()
                .forEach(x ->
                {
                    ProductAmountForRentalDTO dto = productAmountForRentalMap.get(x.getProductCd());
                    int calculation = x.getUsedMonthly() == 1 ?
                            dto.getAmountMonthly() : // 한달을 모두 채웠을때 한달치 요금
                            x.getUsedDays() * dto.getAmountDaily(); // 사용기간을 한달을 못 채웟을때 일일 금액 * 사용일
                    x.setCalculationAmount(calculation);
                });

        log.debug("targetRentalContracts:{}", targetRentalContracts);


        // 제품별 렌탈 비용 계산
        List<RentalProductForCalculationDTO> calcProductRentalFeeList
                = this.calcProductRentalFee(targetRentalContracts);

//        쓰기작업만 트랜잭션으로 묶어 처리
        transactionTemplate.execute(status -> {
            // 렌탈 계약 사용금액 적산
            rentalService.calcRentalAmount(targetRentalContracts);

            //제품별 렌탈 비용 적산
            productService.calcRentalAmount(calcProductRentalFeeList);
            return null;
        });

        return 0;
    }

    private List<RentalProductForCalculationDTO> calcProductRentalFee(List<RentalContractForCalculationDTO> targetRentalContracts) {
        List<Long> contratIdList = targetRentalContracts.stream()
                .map(RentalContractForCalculationDTO::getId)
                .map(Long::valueOf)
                .distinct()
                .toList();

        // 계약건에 해당하는 렌탈(아이템)정보를 가지고 옴
        Map<Long, List<RentalProductForCalculationDTO>> rentalProductMap
                = rentalService.selectRentalProduct(contratIdList);

        //제품별 렌탈비 계산 리스트
        List<RentalProductForCalculationDTO> calcProductRentalFeeList = new ArrayList<>();

        targetRentalContracts.forEach(x -> {
            List<RentalProductForCalculationDTO> dtos = rentalProductMap.get((long)x.getId());

            if (dtos != null) {
                if (dtos.size() == 1) {
                    RentalProductForCalculationDTO dto = dtos.get(0);
                    dto.setCalcAmount(x.getCalculationAmount());

                    calcProductRentalFeeList.add(dto);
                } else {
                    long totalUsedDays = x.getUsedDays();
                    long remainDays = totalUsedDays;
                    long totalAmount = x.getCalculationAmount();
                    long remainAmount = totalAmount;
                    boolean isFirst = true;
                    LocalDate calculationDate = x.getCalculationDate();

                    for (int i = 0; i < dtos.size(); i++) {
                        RentalProductForCalculationDTO dto = dtos.get(i);
                        if (isFirst) {
                            long usedDays = dto.getStartDate().isAfter(x.getCalculationDate()) ?
                                    ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1 :
                                    ChronoUnit.DAYS.between(x.getCalculationDate(), dto.getEndDate()) + 1;
                            remainDays -= usedDays;

                            long calcAmount = x.getCalculationAmount() / totalUsedDays * usedDays;
                            remainAmount -= calcAmount;
                            dto.setCalcAmount((int) calcAmount);
                            dto.setUsedDate((int) usedDays);

                            isFirst = false;
                        } else if (i == dtos.size() - 1) {
                            dto.setCalcAmount((int) remainAmount);
                            dto.setUsedDate((int) remainDays);
                        } else {
                            long usedDays = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate());

                            remainDays -= usedDays;
                            long calcAmount = x.getCalculationAmount() / totalUsedDays * usedDays;
                            remainAmount -= calcAmount;
                            dto.setCalcAmount((int) calcAmount);
                            dto.setUsedDate((int) usedDays);
                        }

                        calcProductRentalFeeList.add(dto);
                    }
                }
            }
        });

        log.debug("calcProductRentalFeeList:{}", calcProductRentalFeeList);
        return calcProductRentalFeeList;
    }
}
