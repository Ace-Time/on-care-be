package org.ateam.oncare.matching.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BeneficiaryPageResponse {
    private List<BeneficiarySummaryDto> content;
    private int page;
    private int size;
    private long total;
}