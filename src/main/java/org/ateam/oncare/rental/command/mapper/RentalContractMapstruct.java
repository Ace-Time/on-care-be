package org.ateam.oncare.rental.command.mapper;

import org.ateam.oncare.rental.command.dto.RequestRentalContractDTO;
import org.ateam.oncare.rental.command.dto.ResponseRentalContractDTO;
import org.ateam.oncare.rental.command.entity.RentalContract;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface RentalContractMapstruct {
    ResponseRentalContractDTO toConractDTO(RentalContract entity);
    RentalContract toConractEntity(RequestRentalContractDTO dto);
}
