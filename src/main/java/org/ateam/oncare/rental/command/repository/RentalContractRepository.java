package org.ateam.oncare.rental.command.repository;

import org.ateam.oncare.rental.command.entity.RentalContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalContractRepository
        extends JpaRepository<RentalContract, Integer>, RentalContractRepositoryCustom {
}
