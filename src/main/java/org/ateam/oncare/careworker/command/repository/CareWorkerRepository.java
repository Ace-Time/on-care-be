package org.ateam.oncare.careworker.command.repository;

import org.ateam.oncare.careworker.command.entity.CareWorker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareWorkerRepository extends JpaRepository<CareWorker, Long> {
}