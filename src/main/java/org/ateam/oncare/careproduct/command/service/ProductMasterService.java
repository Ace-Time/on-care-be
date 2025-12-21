package org.ateam.oncare.careproduct.command.service;

import org.ateam.oncare.careproduct.command.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ProductMasterService {
    Slice<ResponseProductMasterDTO> getProductMaster(RequestProductMasterForSelectDTO condition, Pageable pageable);

    List<ResponseMasterCategoryDTO> getMasterCategory();

    int updateProductMaster(RequestProductMasterDTO requestProductMasterDTO);

    Slice<ResponseProductMasterDetailDTO> getProductMasterDetail(RequestProductMasterForSelectDTO condition, Pageable pageable);
}
