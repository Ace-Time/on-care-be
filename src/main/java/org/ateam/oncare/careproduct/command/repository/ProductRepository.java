package org.ateam.oncare.careproduct.command.repository;

import org.ateam.oncare.careproduct.command.entity.CareProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<CareProduct, String>, ProductRepositoryCustom {
}
