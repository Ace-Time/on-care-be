package org.ateam.oncare.careproduct.mapper;

import org.ateam.oncare.careproduct.command.dto.ResponseProductMasterDTO;
import org.ateam.oncare.careproduct.command.entity.CareProductMaster;
import org.mapstruct.Mapper;

@Mapper(componentModel= "spring")
public interface ProductMasterMapper {
    ResponseProductMasterDTO toProductMasterDTO(CareProductMaster entity);
}
