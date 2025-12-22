package org.ateam.oncare.rental.command.repository;

import org.ateam.oncare.rental.command.entity.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractStatusRepoistory extends JpaRepository<ContractStatus, Long> {
}
