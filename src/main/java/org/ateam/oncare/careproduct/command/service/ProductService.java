package org.ateam.oncare.careproduct.command.service;

import org.ateam.oncare.careproduct.command.dto.RequestProductForSelectDTO;
import org.ateam.oncare.careproduct.command.dto.RequestProductHistoryDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductHistroyDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductService {
    Slice<ResponseProductDTO> getProduct(RequestProductForSelectDTO condition, Pageable pageable);

    Slice<ResponseProductHistroyDTO> getProductHistory(RequestProductHistoryDTO condition, Pageable pageable);
}
