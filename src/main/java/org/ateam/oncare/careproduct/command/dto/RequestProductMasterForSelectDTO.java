package org.ateam.oncare.careproduct.command.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RequestProductMasterForSelectDTO {
    private String codeOrName; //코드 혹은 명칭으로 조회
    private String categoryCode;
}
