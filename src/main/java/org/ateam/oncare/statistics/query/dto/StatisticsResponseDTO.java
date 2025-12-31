package org.ateam.oncare.statistics.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StatisticsResponseDTO {
    private List<ProductStatisticDTO> highReturnProducts;
    private List<ProductStatisticDTO> lowReturnProducts;
    private List<ProductStatisticDTO> allProductStats;
}
