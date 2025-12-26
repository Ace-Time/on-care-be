package org.ateam.oncare.beneficiary.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.FormListItem;
import org.ateam.oncare.beneficiary.query.mapper.FormQueryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormQueryService {

    private final FormQueryMapper mapper;

    public List<FormListItem> getFormsForBeneficiary(Long beneficiaryId) {
        return mapper.selectFormsForBeneficiary(beneficiaryId);
    }
}