package org.ateam.oncare.config.customexception;

import org.springframework.stereotype.Component;

public class ExistsAlreadyProductMasterException extends RuntimeException {

    public ExistsAlreadyProductMasterException(String message) {
        super(message);
    }
}
