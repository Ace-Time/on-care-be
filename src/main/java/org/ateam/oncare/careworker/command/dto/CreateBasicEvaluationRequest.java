package org.ateam.oncare.careworker.command.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBasicEvaluationRequest {
    private String evalType;  // FALL, BEDSORE, COGNITIVE, NEEDS
    private Long templateId;
    private LocalDate evalDate;

    @Setter
    private String evalData;  // JSON 형식

    private Long beneficiaryId;

    @Setter
    private String specialNote;  // 특이사항

    // JSON 객체를 문자열로 자동 변환
    @JsonSetter("evalData")
    public void setEvalData(Object evalData) {
        if (evalData == null) {
            this.evalData = null;
        } else if (evalData instanceof String) {
            this.evalData = (String) evalData;
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                this.evalData = mapper.writeValueAsString(evalData);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Invalid evalData format", e);
            }
        }
    }

    public void setEvalType(String evalType) {
        this.evalType = evalType;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public void setEvalDate(LocalDate evalDate) {
        this.evalDate = evalDate;
    }

    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }
}
