package org.ateam.oncare.careproduct.mapper;

import org.ateam.oncare.careproduct.command.dto.ResponseProductHistoryDTO;
import org.ateam.oncare.careproduct.command.entity.ProductHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ProductHistoryMapper {
    ResponseProductHistoryDTO toHistoryDTO(ProductHistory entity);
}
