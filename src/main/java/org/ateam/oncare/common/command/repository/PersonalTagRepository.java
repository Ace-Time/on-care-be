package org.ateam.oncare.common.command.repository;

import org.ateam.oncare.common.command.entity.PersonalTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonalTagRepository extends JpaRepository<PersonalTag,Long> {
    @Query("SELECT p.id FROM PersonalTag p WHERE p.tag = :tag")
    Optional<Long> findIdByTag(@Param("tag") String name);
}
