package org.ateam.oncare.rental.command.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.rental.command.repository.RentalContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalContractRepository repository;

    @Override
    public Map<String, Long> getExpectedToShip() {
        Map<String, Long> expectation = repository.selectExpectedToShip();
        return expectation;
    }
}
