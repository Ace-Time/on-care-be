package org.ateam.oncare.beneficiary.query.dto.response;

//기초평가 (욕구)

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NeedsEvaluationLatestResponse {

    private Long evalId;
    private String evalDate; // yyyy-MM-dd

    private Long beneficiaryId;
    private String beneficiaryName;

    private Integer careWorkerId;
    private String careWorkerName;

    private String subjectiveNeed; // answers."8".subjective_need
    private String summary;        // answers."9".summary
}
