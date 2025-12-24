package org.ateam.oncare.rental.command.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.careproduct.command.dto.RequestProductMasterForSelectDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductMasterDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductMasterDetailDTO;
import org.ateam.oncare.global.enums.StockType;
import org.ateam.oncare.global.eventType.ProductStockEvent;
import org.ateam.oncare.rental.command.dto.*;
import org.ateam.oncare.rental.command.entity.ContractStatus;
import org.ateam.oncare.rental.command.entity.RentalContract;
import org.ateam.oncare.rental.command.entity.RentalProduct;
import org.ateam.oncare.rental.command.facade.RentalFacade;
import org.ateam.oncare.rental.command.mapper.RentalContractMapstruct;
import org.ateam.oncare.rental.command.repository.ContractStatusRepoistory;
import org.ateam.oncare.rental.command.repository.RentalContractRepository;
import org.ateam.oncare.rental.command.repository.RentalProductRepository;
import org.ateam.oncare.rental.command.repository.RentalProductStatusRepository;
import org.ateam.oncare.rental.query.service.RentalQueryService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalServiceImpl implements RentalService {
    private final RentalContractRepository contractRepository;
    private final RentalProductRepository rentalProductRepository;
    private final ContractStatusRepoistory contractStatusRepoistory;
    private final RentalQueryService rentalQueryService;
    private final RentalProductStatusRepository rentalProductStatusRepository;
    private final RentalContractMapstruct rentalContractMapstruct;
    private final ApplicationEventPublisher applicationEventPublisher; // 변경 사항을 알리기 위함.

    @Override
    public Map<String, Long> getExpectedToShip() {
        Map<String, Long> expectation = contractRepository.selectExpectedToShip();
        return expectation;
    }

    @Override
    @Cacheable(value = "masterData", key = "'contract_status'")
    public List<ResponseContractStatusType> getContractStatusType() {

        List<ContractStatus> entities = contractStatusRepoistory.findAll();
        List<ResponseContractStatusType> reponse = entities.stream()
                .map(e -> new ResponseContractStatusType(e.getId(), e.getName()))
                .toList();

        return reponse;
    }

    @Override
    public Slice<ResponseContractRentalDTO> getContract(RequestContractDTO condition, Pageable pageable) {
        Slice<ResponseContractRentalDTO> response =
                rentalQueryService.getContract(condition, pageable);

        return response;
    }

    @Override
    public void calcRentalAmount(List<RentalContractForCalculationDTO> targetRentalContracts) {
        contractRepository.calculateContratMonthlyForRental(targetRentalContracts);
    }

    @Override
    public Map<Long, List<RentalProductForCalculationDTO>> selectRentalProduct(List<Long> contratIdList) {
        Map<Long, List<RentalProductForCalculationDTO>> rental = rentalProductRepository.selectRentalProduct(contratIdList);

        return rental;
    }

    @Override
    public List<ResponseRentalProductDTO> getProductRental(Long contractCode) {
        List<RentalProduct> entities
                = rentalProductRepository.findAllByRentalContractCd(contractCode);
        List<ResponseRentalProductDTO> results
                = entities.stream()
                .map(e -> ResponseRentalProductDTO.builder()
                        .id(e.getId())
                        .productId(e.getProductId())
                        .rentalContractCd(e.getRentalContractCd())
                        .endDate(e.getEndDate())
                        .startDate(e.getStartDate())
                        .rentalStatusId(e.getRentalStatusId())
                        .build()
                ).toList();

        return results;
    }

    @Override
    @Cacheable(value = "masterData", key = "'rental_product_status'")
    public List<ResponseRetnalProductStatusType> getRentalProductStatus() {
        List<ResponseRetnalProductStatusType> rentalProductStatus
                = rentalProductStatusRepository.findAll()
                .stream()
                .map(x -> new ResponseRetnalProductStatusType(
                        x.getId(),
                        x.getName()
                )).toList();
        return rentalProductStatus;
    }

    @Override
    public ResponseRentalContractDTO registRentalContract(RequestRentalContractDTO request) {
        RentalContract requestEitnty = rentalContractMapstruct.toConractEntity(request);
        RentalContract responseEntity = contractRepository.save(requestEitnty);
        ResponseRentalContractDTO responseDTO = rentalContractMapstruct.toConractDTO(responseEntity);

        log.debug("responseDTO:{}", responseDTO);

        return responseDTO;
    }
}
