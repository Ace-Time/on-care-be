package org.ateam.oncare.careproduct.command.repository;

import org.ateam.oncare.careproduct.command.dto.RequestProductHistoryDTO;
import org.ateam.oncare.careproduct.command.dto.ResponseProductHistoryDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductHistoryRepositoryCustom {
    Slice<ResponseProductHistoryDTO> getProductHistories(RequestProductHistoryDTO condition, Pageable pageable);
}
