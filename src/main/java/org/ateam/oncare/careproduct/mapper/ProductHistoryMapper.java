package org.ateam.oncare.careproduct.mapper;

import org.ateam.oncare.careproduct.command.dto.ResponseProductHistroyDTO;
import org.ateam.oncare.careproduct.command.entity.ProductHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ProductHistoryMapper {
    ResponseProductHistroyDTO toHistoryDTO(ProductHistory entity);
}
