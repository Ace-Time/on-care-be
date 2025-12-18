package org.ateam.oncare.counsel.command.service;

import org.ateam.oncare.counsel.command.dto.GeneralCounselResponse;
import org.ateam.oncare.counsel.command.dto.RegistGeneralCounselRequest;

public interface CounselCommandService {
    GeneralCounselResponse registGeneralCounsel(RegistGeneralCounselRequest request);
}
