package org.ateam.oncare.careproduct.command.repository;

import org.ateam.oncare.careproduct.command.entity.CareProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<CareProduct, String>, ProductRepositoryCustom {
    List<CareProduct> findByProductCdAndProductStatus(String productCode, int i);
}
