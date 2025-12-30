package org.ateam.oncare.careproduct.command.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class AggregationOfProductDTO {
    private String productCode;
    private int total;
    private int available;
    private int rental;
    private int discard;
    private int reserved;

    @QueryProjection
    public AggregationOfProductDTO(String productCode, int total, int available, int rental, int discard, int reserved) {
        this.productCode = productCode;
        this.total = total;
        this.available = available;
        this.rental = rental;
        this.reserved = reserved;
        this.discard = discard;
    }
}
