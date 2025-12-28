package org.ateam.oncare.matching.command.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.matching.command.repository.MatchingRepository;
import org.ateam.oncare.schedule.command.entity.Matching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingAssignService {

    private final MatchingRepository matchingRepository;

    public void assign(Long beneficiaryId, Long careWorkerId) {

        matchingRepository.findByBeneficiaryIdAndStatus(beneficiaryId, "Y")
                .ifPresentOrElse(existing -> {

                    if (existing.getCareWorkerId().equals(careWorkerId)) {
                        return;
                    }

                    existing.setStatus("N");
                    existing.setEndDate(LocalDate.now());
                    existing.setReason("요양보호사 변경");
                    matchingRepository.save(existing);

                    Matching newMatching = new Matching();
                    newMatching.setBeneficiaryId(beneficiaryId);
                    newMatching.setCareWorkerId(careWorkerId);
                    newMatching.setStartDate(LocalDate.now());
                    newMatching.setEndDate(null);
                    newMatching.setStatus("Y");
                    newMatching.setReason(null);

                    matchingRepository.save(newMatching);

                }, () -> {

                    Matching newMatching = new Matching();
                    newMatching.setBeneficiaryId(beneficiaryId);
                    newMatching.setCareWorkerId(careWorkerId);
                    newMatching.setStartDate(LocalDate.now());
                    newMatching.setEndDate(null);
                    newMatching.setStatus("Y");
                    newMatching.setReason(null);

                    matchingRepository.save(newMatching);
                });
    }
}