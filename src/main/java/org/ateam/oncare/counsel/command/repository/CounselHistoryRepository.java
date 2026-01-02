package org.ateam.oncare.counsel.command.repository;

import org.ateam.oncare.counsel.command.entity.CounselHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselHistoryRepository extends JpaRepository<CounselHistory,Long> {
}
