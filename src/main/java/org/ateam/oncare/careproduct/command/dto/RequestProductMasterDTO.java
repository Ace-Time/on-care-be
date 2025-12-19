package org.ateam.oncare.careproduct.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RequestProductMasterDTO {
    private String codeOrName; //코드 혹은 명칭으로 조회
    private String categoryName;
}
