package org.ateam.oncare.careproduct.command.repository;

import org.ateam.oncare.careproduct.command.entity.CareProductMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMasterRepository extends JpaRepository<CareProductMaster, String> , ProductMasterRepositoryCustom{

}
