package org.ateam.oncare.beneficiary.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CognitiveEvaluationLatestResponse {

    private Long evalId;
    private String evalDate; // yyyy-MM-dd

    private Long beneficiaryId;
    private String beneficiaryName;

    private Integer careWorkerId;
    private String careWorkerName;

    private String educationLabel;

    private String aTitle;
    private Integer aScore;

    private String cTitle;
    private Integer cScore;

    private String dTitle;
    private Integer dScore;

    private String eTitle;
    private Integer eScore;

    /*  F 영역 추가 */
    private String fTitle;          // sections.F.title
    private Integer fRawTotal;      // sections.F.raw_total
    private Integer fConvertedTotal;// sections.F.converted_total

    private String gTitle;
    private Integer gScore;

    private Integer totalScore;

    private String interpretationResult;
    private String interpretationComment;
}
