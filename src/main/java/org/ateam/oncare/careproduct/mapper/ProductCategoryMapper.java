package org.ateam.oncare.careproduct.mapper;

import org.ateam.oncare.careproduct.command.dto.ResponseMasterCategoryDTO;
import org.ateam.oncare.careproduct.command.entity.ProductCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ProductCategoryMapper {
    ResponseMasterCategoryDTO toCategoryDTO(ProductCategory productCategory);
}
