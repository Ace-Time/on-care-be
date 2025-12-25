package org.ateam.oncare.rental.command.mapper;

import org.ateam.oncare.rental.command.dto.RequestRentalContractDTO;
import org.ateam.oncare.rental.command.dto.ResponseRentalContractDTO;
import org.ateam.oncare.rental.command.entity.RentalContract;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

// null 인거는 무시 함(nullValuePropertyMappingStrategy)
@Mapper(componentModel="spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RentalContractMapstruct {
    ResponseRentalContractDTO toConractDTO(RentalContract entity);

    RentalContract toConractEntity(RequestRentalContractDTO dto);
    void updateFromDTO(RequestRentalContractDTO dto, @MappingTarget RentalContract entity);
}
