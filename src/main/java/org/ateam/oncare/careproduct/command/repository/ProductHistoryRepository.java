package org.ateam.oncare.careproduct.command.repository;

import org.ateam.oncare.careproduct.command.entity.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory,Integer>, ProductHistoryRepositoryCustom {
}
