package org.ateam.oncare.global.eventType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ateam.oncare.global.enums.MasterInternalType;

@Getter
@AllArgsConstructor
public class MasterDataEvent {
    private MasterInternalType type;
}
