package org.ateam.oncare.careproduct.mapper;

import org.ateam.oncare.careproduct.command.entity.ProductTask;
import org.ateam.oncare.global.enums.StockType;
import org.ateam.oncare.global.eventType.ProductStockEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface ProductTaskMapStruct {
    @Mapping(source="status", target="status" )
    @Mapping(source="expectedDate", target="expectedDate" )
    @Mapping(source="isConfirmed", target="isConfirmed" )
    ProductTask toEntity(ProductStockEvent event);

    default Integer toInteger(StockType value) {
        return value.ordinal();
    }
}
