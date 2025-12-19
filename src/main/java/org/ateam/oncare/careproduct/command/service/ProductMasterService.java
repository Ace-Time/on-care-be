package org.ateam.oncare.careproduct.command.service;

import org.ateam.oncare.careproduct.command.dto.RequestProductMasterDTO;
import org.ateam.oncare.careproduct.command.dto.RequestProductMasterForSelectDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseMasterCategoryDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductMasterDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ProductMasterService {
    Slice<ResponseProductMasterDTO> getProductMaster(RequestProductMasterForSelectDTO condition, Pageable pageable);

    List<ResponseMasterCategoryDTO> getMasterCategory();

    int updateProductMaster(RequestProductMasterDTO requestProductMasterDTO);
}
