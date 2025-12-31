package org.ateam.oncare.counsel.command.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;

public interface CounselHistoryRequired {
    LocalDateTime getConsultDate();
    String getDetail();
    int getGuardianSt();
    String getFollowUp();
    String getFollowUpNecessary();
    String getChurn();
    String getChurnReason();
    int getReservationChannelId();
    int getCounselCategoryId();
    int getEmployeeId();

    // 고객 식별용
    String getCustomerType();
    BigInteger getCustomerId();
}
