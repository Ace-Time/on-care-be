package org.ateam.oncare.careproduct.command.repository;

import org.ateam.oncare.careproduct.command.entity.ProductTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTaskRepository extends JpaRepository<ProductTask, String> {
}
