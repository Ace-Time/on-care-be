package org.ateam.oncare.beneficiary.command.repository;

import org.ateam.oncare.beneficiary.command.entity.RiskOfMember;
import org.ateam.oncare.beneficiary.command.entity.RiskOfMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskOfMemberRepository extends JpaRepository<RiskOfMember, RiskOfMemberId> {
}
