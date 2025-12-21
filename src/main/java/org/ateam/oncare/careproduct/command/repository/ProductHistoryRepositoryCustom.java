package org.ateam.oncare.careproduct.command.repository;

import org.ateam.oncare.careproduct.command.dto.RequestProductHistoryDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductHistroyDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductHistoryRepositoryCustom {
    Slice<ResponseProductHistroyDTO> getProductHistories(RequestProductHistoryDTO condition, Pageable pageable);
}
