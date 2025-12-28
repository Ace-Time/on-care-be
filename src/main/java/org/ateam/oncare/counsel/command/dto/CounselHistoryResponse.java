package org.ateam.oncare.counsel.command.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;

public interface CounselHistoryResponse {
    LocalDateTime getConsultDate();
    String getDetail();
    String getFollowUp();
    String getFollowUpNecessary();
    String getChurn();
    String getChurnReason();
    int getReservationChannelId();
    int getPotentialId();
    int getBeneficiaryId();
    int getCounselCategoryId();
    int getCounselorId();
}
