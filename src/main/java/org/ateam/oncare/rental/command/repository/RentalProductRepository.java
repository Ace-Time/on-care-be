package org.ateam.oncare.rental.command.repository;

import org.ateam.oncare.rental.command.dto.ResponseRentalProductDTO;
import org.ateam.oncare.rental.command.entity.RentalProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalProductRepository extends JpaRepository<RentalProduct, Integer>,RentalProductRepositoryCustom {
    List<RentalProduct> findAllByRentalContractCd(Long contractCode);
}
