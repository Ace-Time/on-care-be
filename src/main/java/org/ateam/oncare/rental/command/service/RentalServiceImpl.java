package org.ateam.oncare.rental.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.rental.command.dto.RequestContractDTO;
import org.ateam.oncare.rental.command.dto.ResponseContractRentalDTO;
import org.ateam.oncare.rental.command.dto.ResponseContractStatusType;
import org.ateam.oncare.rental.command.entity.ContractStatus;
import org.ateam.oncare.rental.command.repository.ContractStatusRepoistory;
import org.ateam.oncare.rental.command.repository.RentalContractRepository;
import org.ateam.oncare.rental.query.service.RentalQueryService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalContractRepository contractRepository;
    private final ContractStatusRepoistory contractStatusRepoistory;
    private final RentalQueryService rentalQueryService;

    @Override
    public Map<String, Long> getExpectedToShip() {
        Map<String, Long> expectation = contractRepository.selectExpectedToShip();
        return expectation;
    }

    @Override
    @Cacheable(value="masterData" , key="'contract_status'")
    public List<ResponseContractStatusType> getContractStatusType() {

        List<ContractStatus> entities = contractStatusRepoistory.findAll();
        List<ResponseContractStatusType> reponse = entities.stream()
                .map(e -> new ResponseContractStatusType(e.getId(),e.getName()))
                .toList();

        return reponse;
    }

    @Override
    public Slice<ResponseContractRentalDTO> getContract(RequestContractDTO condition, Pageable pageable) {
        Slice<ResponseContractRentalDTO> response =
                rentalQueryService.getContract(condition,pageable);

        return response;
    }
}
