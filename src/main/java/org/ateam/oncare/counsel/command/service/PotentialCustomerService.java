package org.ateam.oncare.counsel.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.counsel.command.dto.Subscription;
import org.ateam.oncare.counsel.command.entity.PotentialCustomer;
import org.ateam.oncare.counsel.command.repository.PotentialCustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PotentialCustomerService {
    private final PotentialCustomerRepository potentialCustomerRepository;

    public BigInteger registPotentialCustomer(String name, String phone) {

        PotentialCustomer potentialCustomer = PotentialCustomer.builder()
                .name(name)
                .phone(phone)
                .createAt(LocalDateTime.now())
                .lastCounselDate(null)
                .currentStage(1)
                .build();

        // 반환하는 객체를 받기
        PotentialCustomer savedCustomer = potentialCustomerRepository.save(potentialCustomer);

        return BigInteger.valueOf(savedCustomer.getId());
    }
}
