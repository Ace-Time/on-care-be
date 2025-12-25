package org.ateam.oncare.rental.command.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.ateam.oncare.careproduct.command.dto.RequestProductHistoryDTO;
import org.ateam.oncare.careproduct.command.service.ProductMasterService;
import org.ateam.oncare.rental.command.dto.*;
import org.ateam.oncare.rental.command.enums.ContractStatusType;
import org.ateam.oncare.rental.command.facade.RentalFacade;
import org.ateam.oncare.rental.command.service.RentalService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
@Slf4j
public class RentalController {
    private final RentalService rentalService;
    private final RentalFacade rentalFacade;

    @GetMapping("/rental")
    public ResponseEntity<Slice<ResponseContractRentalDTO>> getContractRental(
            RequestContractDTO condition,
            @PageableDefault(size = 10) Pageable pageable) {

        log.debug("condition:{}",condition);
        Slice<ResponseContractRentalDTO> respose = rentalService.getContract(condition,pageable);

        System.out.println(ContractStatusType.values()[1]);
        System.out.println(ContractStatusType.유지.ordinal());
        System.out.println(ContractStatusType.유지.name());
        System.out.println(ContractStatusType.valueOf("접수"));

        return ResponseEntity.ok(respose);
    }

    @GetMapping("/rental-product")
    public ResponseEntity<List<ResponseRentalProductDTO>> getProductRental(
            Long contractCode
    ){
        List<ResponseRentalProductDTO> responseDTO = rentalService.getProductRental(contractCode);

        return ResponseEntity.ok()
                .body(responseDTO);
    }


    @GetMapping("/contract/type")
    public ResponseEntity<List<ResponseContractStatusType>> getContractType() {
        List<ResponseContractStatusType> response = rentalService.getContractStatusType();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rental-product/status")
    public ResponseEntity<List<ResponseRetnalProductStatusType>> getRentalProductStatus() {
        List<ResponseRetnalProductStatusType> response = rentalService.getRentalProductStatus();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/calculation/{calcDate}")
    public ResponseEntity<Integer> calculationRentalAmount(@PathVariable LocalDate calcDate) {
        int count = rentalFacade.calcRentalAmount(calcDate);
        log.debug("calculationRentalAmount:{}",calcDate);
        return ResponseEntity.ok(1);
    }

    @PostMapping("/contract")
    public ResponseEntity<ResponseRentalContractDTO> registRentalContract(@RequestBody RequestRentalContractDTO request) {
        ResponseRentalContractDTO responseDTO = rentalFacade.registRentalContract(request);

        return ResponseEntity.ok()
                .body(responseDTO);
    }

    @PatchMapping("/contract")
    public ResponseEntity<ResponseRentalContractDTO> updateContract(@RequestBody RequestRentalContractDTO request) {
        ResponseRentalContractDTO responseDTO = rentalService.updateContract(request);

        return ResponseEntity.ok()
                .body(responseDTO);
    }

    @PatchMapping("/contract/termination")
    public ResponseEntity<ResponseRentalContractDTO> terminateContract(@RequestBody RequestRentalContractDTO request) {
        ResponseRentalContractDTO responseDTO = rentalFacade.terminateContract(request);

        return ResponseEntity.ok()
                .body(responseDTO);
    }


}
