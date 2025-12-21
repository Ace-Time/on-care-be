package org.ateam.oncare.careproduct.command.repository;

import org.ateam.oncare.careproduct.command.dto.RequestProductMasterForSelectDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductMasterDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductMasterDetailDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductMasterRepositoryCustom {
    Slice<ResponseProductMasterDTO> selectProductMaster(RequestProductMasterForSelectDTO condition, Pageable pageable);

    Slice<ResponseProductMasterDetailDTO> selectProductMasterDetail(RequestProductMasterForSelectDTO condition, Pageable pageable);
}
