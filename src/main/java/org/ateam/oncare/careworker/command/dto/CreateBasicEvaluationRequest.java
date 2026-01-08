package org.ateam.oncare.careworker.command.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore extra fields like careWorkerName, totalScore
public class CreateBasicEvaluationRequest {

    // Fields match the JSON keys (camelCase)
    private Long beneficiaryId;
    private LocalDate evalDate; // Matches "evalDate" in JSON
    private String evalData;
    private Boolean isDraft;

    // Some fields might not be in the JSON or have different names, keep them
    // nullable
    private String evalType;
    private Long templateId;
    private String specialNote;

    // --- Explicit Getters and Setters matching JSON keys (camelCase) ---

    // JSON key: beneficiaryId
    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public Long getBeneficiaryId() {
        return this.beneficiaryId;
    }

    // JSON key: evalDate
    public void setEvalDate(LocalDate evalDate) {
        this.evalDate = evalDate;
    }

    public LocalDate getEvalDate() {
        return this.evalDate;
    }

    // JSON key: isDraft
    public void setIsDraft(Boolean isDraft) {
        this.isDraft = isDraft;
    }

    public Boolean getIsDraft() {
        return this.isDraft;
    }

    // JSON key: evalType (if present)
    public void setEvalType(String evalType) {
        this.evalType = evalType;
    }

    public String getEvalType() {
        return this.evalType;
    }

    // JSON key: templateId (if present)
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getTemplateId() {
        return this.templateId;
    }

    // JSON key: specialNote (if present)
    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public String getSpecialNote() {
        return this.specialNote;
    }

    // Custom logic for evalData
    // We bind to "evalData" (camelCase) coming from JSON
    @JsonProperty("evalData")
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

    public String getEvalData() {
        return this.evalData;
    }

    @Override
    public String toString() {
        return "CreateBasicEvaluationRequest{" +
                "evalType='" + evalType + '\'' +
                ", templateId=" + templateId +
                ", evalDate=" + evalDate +
                ", evalData='" + evalData + '\'' +
                ", beneficiaryId=" + beneficiaryId +
                ", specialNote='" + specialNote + '\'' +
                ", isDraft=" + isDraft +
                '}';
    }
}
