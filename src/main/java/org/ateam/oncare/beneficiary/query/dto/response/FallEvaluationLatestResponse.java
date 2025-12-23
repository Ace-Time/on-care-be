package org.ateam.oncare.beneficiary.query.dto.response;

// 기초평가 (낙상)

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FallEvaluationLatestResponse {

    private Long evalId;
    private String evalDate; // yyyy-MM-dd

    private Long beneficiaryId;
    private String beneficiaryName;

    private Integer careWorkerId;
    private String careWorkerName;

    private Integer totalScore;
    private String grade;

    private List<AnswerItem> answers = new ArrayList<>();

    @Getter
    @Setter
    public static class AnswerItem {
        private String questionLabel;  // answers[].label
        private String selectedLabel;  // answers[].selected.label
        private Integer selectedScore; // answers[].selected.score
    }
}
