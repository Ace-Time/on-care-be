package org.ateam.oncare.careproduct.mapper;

import org.ateam.oncare.careproduct.command.dto.ResponseProductStatusDTO;
import org.ateam.oncare.careproduct.command.entity.ProductStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ProductStatusMapstruct {
    ResponseProductStatusDTO toDTO(ProductStatus entity);
}
