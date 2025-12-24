package org.ateam.oncare.rental.command.repository;

import org.ateam.oncare.rental.command.entity.RentalProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalProductRepository extends JpaRepository<RentalProduct, Integer>,RentalProductRepositoryCustom {
}
