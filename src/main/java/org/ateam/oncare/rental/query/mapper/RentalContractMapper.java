package org.ateam.oncare.rental.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.rental.command.dto.RequestContractDTO;
import org.ateam.oncare.rental.command.dto.ResponseContractRentalDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface RentalContractMapper {
    List<ResponseContractRentalDTO> selectRentalContract(
            @Param("cond") RequestContractDTO cond,
            @Param("page") Pageable page);
}
