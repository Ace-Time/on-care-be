package org.ateam.oncare.careproduct.command.service;

import org.ateam.oncare.careproduct.command.dto.RequestProductForSelectDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductService {
    Slice<ResponseProductDTO> getProduct(RequestProductForSelectDTO condition, Pageable pageable);
}
