package org.ateam.oncare.careproduct.mapper;

import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.ateam.oncare.careproduct.command.dto.ResponseProductDTO;
import org.ateam.oncare.careproduct.command.entity.CareProduct;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ProductMapper {
    ResponseProductDTO toProductDTO(CareProduct product);
}
