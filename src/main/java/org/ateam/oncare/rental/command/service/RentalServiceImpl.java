package org.ateam.oncare.rental.command.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.config.customexception.InvalidRentalDateException;
import org.ateam.oncare.global.enums.StockType;
import org.ateam.oncare.global.eventType.ProductStockEvent;
import org.ateam.oncare.rental.command.dto.*;
import org.ateam.oncare.rental.command.entity.ContractStatus;
import org.ateam.oncare.rental.command.entity.RentalContract;
import org.ateam.oncare.rental.command.entity.RentalProduct;
import org.ateam.oncare.rental.command.mapper.RentalContractMapstruct;
import org.ateam.oncare.rental.command.mapper.RentalProductMapStruct;
import org.ateam.oncare.rental.command.repository.ContractStatusRepoistory;
import org.ateam.oncare.rental.command.repository.RentalContractRepository;
import org.ateam.oncare.rental.command.repository.RentalProductRepository;
import org.ateam.oncare.rental.command.repository.RentalProductStatusRepository;
import org.ateam.oncare.rental.query.service.RentalQueryService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final ApplicationEventPublisher eventPublisher; // 변경 사항을 알리기 위함.
    private final RentalProductMapStruct rentalProductMapStruct;

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

        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseRentalContractDTO updateContract(RequestRentalContractDTO request) {
        if(request.getWantedDate() != null && request.getWantedDate().isBefore(LocalDate.now()))
            throw new InvalidRentalDateException("현재 보다 과거로 렌탈 시작 날짜 변경 불가");
        if(request.getExpectedDate() != null && request.getExpectedDate().isBefore(LocalDate.now()))
            throw new InvalidRentalDateException("현재 보다 과거로 렌탈 종료 날짜 변경 불가");

        RentalContract entity = contractRepository.findById(request.getId().intValue()).get();
        rentalContractMapstruct.updateFromDTO(request, entity);


        // 렌탈 계약 종료 처리 후 철회 하는 경우, 용품 입고 리스트에서 제거
        if(request.getContractStatusCd() != null && request.getContractStatusCd() == 2){

            RentalProduct rentalProduct = rentalProductRepository
                    .findByRentalContractCdAndRentalStatusId(request.getId(),2);

            if(rentalProduct == null)
                return null;

            // 철회시 종료일자 초기화
            entity.setEndDate(null);
            
            // 회수 접수 상태 -> 유지 상태로 변경
            rentalProduct.setRentalStatusId(1L);
            rentalProductRepository.save(rentalProduct);

            // 입고 예정 리스트에 등록
            ProductStockEvent stockEvent = ProductStockEvent.builder()
                    .status(StockType.Canceled)
                    .productId(rentalProduct.getProductId())
                    .build();

            eventPublisher.publishEvent(stockEvent);
        }

        RentalContract responseEntity = contractRepository.save(entity);
        ResponseRentalContractDTO responseDTO = rentalContractMapstruct.toConractDTO(responseEntity);

        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseRentalContractDTO terminateContract(RequestRentalContractDTO request) {

        RentalContract entity = contractRepository.findById(request.getId().intValue()).get();
        rentalContractMapstruct.updateFromDTO(request, entity);
        RentalContract responseEntity = contractRepository.save(entity);
        ResponseRentalContractDTO responseDTO = rentalContractMapstruct.toConractDTO(responseEntity);
        return responseDTO;
    }

    @Override
    public ResponseRentalProductDTO terminateProduct(Long rnetalProductId) {

        RentalProduct rentalProduct = rentalProductRepository.findByRentalContractCdAndRentalStatusId(rnetalProductId, 1);

        // 유지 상태 상태 -> 회수 접수로 변경
        rentalProduct.setRentalStatusId(2L);
        RentalProduct responseEntity = rentalProductRepository.save(rentalProduct);

        ResponseRentalProductDTO response = rentalProductMapStruct.toProductDTO(responseEntity);

        return response;
    }


}
