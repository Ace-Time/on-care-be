package org.ateam.oncare.careproduct.command.repository;

import org.ateam.oncare.careproduct.command.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductStatusRepository extends JpaRepository<ProductStatus, Long> {
}
