package org.ateam.oncare.careworker.command.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public class UpdateBasicEvaluationRequest {

    private LocalDate evalDate;
    private String evalData;
    private String specialNote;
    private Boolean isDraft;

    // --- Explicit Getters and Setters with @JsonProperty ---

    @JsonProperty("eval_date")
    public void setEvalDate(LocalDate evalDate) {
        this.evalDate = evalDate;
    }

    @JsonProperty("eval_date")
    public LocalDate getEvalDate() {
        return this.evalDate;
    }

    @JsonProperty("special_note")
    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    @JsonProperty("special_note")
    public String getSpecialNote() {
        return this.specialNote;
    }

    @JsonProperty("is_draft")
    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    @JsonProperty("is_draft")
    public Boolean getIsDraft() {
        return this.isDraft;
    }

    // Custom logic for eval_data
    @JsonProperty("eval_data")
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

    @JsonProperty("eval_data")
    public String getEvalData() {
        return this.evalData;
    }
}
