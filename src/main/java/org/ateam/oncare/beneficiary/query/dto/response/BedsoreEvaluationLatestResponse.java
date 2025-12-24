package org.ateam.oncare.beneficiary.query.dto.response;

// 기초평가 (욕창)

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BedsoreEvaluationLatestResponse {

    private Long evalId;
    private String evalDate; // yyyy-MM-dd

    private Long beneficiaryId;
    private String beneficiaryName;

    private Integer careWorkerId;
    private String careWorkerName;

    private Integer totalScore; // eval_data.total_score
    private String grade;       // eval_data.grade
    private String comment;     // eval_data.comment

    private List<AnswerItem> answers = new ArrayList<>();

    @Getter
    @Setter
    public static class AnswerItem {
        private String code;   // answers[].code
        private String label;  // answers[].label
        private Integer score; // answers[].score
    }
}
