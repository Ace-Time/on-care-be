package org.ateam.oncare.rental.command.mapper;

import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.ateam.oncare.rental.command.dto.ResponseRentalProductDTO;
import org.ateam.oncare.rental.command.entity.RentalProduct;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface RentalProductMapStruct {
    ResponseRentalProductDTO toProductDTO(RentalProduct entity);
}
