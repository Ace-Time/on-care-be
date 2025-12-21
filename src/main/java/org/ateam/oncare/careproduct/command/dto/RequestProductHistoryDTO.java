package org.ateam.oncare.careproduct.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RequestProductHistoryDTO {
    private String historyStatus;
    private String productId;
}
