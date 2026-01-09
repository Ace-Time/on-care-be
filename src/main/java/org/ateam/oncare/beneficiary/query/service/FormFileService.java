package org.ateam.oncare.beneficiary.query.service;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.beneficiary.query.dto.FormFileInfo;
import org.ateam.oncare.beneficiary.query.mapper.FormAccessMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FormFileService {

    private final FormAccessMapper accessMapper;

    public FormFileInfo getAllowedFileInfo(Long beneficiaryId, Long formId) {
        return accessMapper.selectAllowedFormFileInfo(beneficiaryId, formId);
    }

    public Resource getResource(FormFileInfo info) {
        String filePath = info.getFilePath(); // 예: "img/beneficiary/template_record_visit.pdf"
        if (filePath == null || filePath.isBlank()) return new ClassPathResource("");

        String normalized = filePath.startsWith("/") ? filePath.substring(1) : filePath;
        return new ClassPathResource(normalized);
    }
}