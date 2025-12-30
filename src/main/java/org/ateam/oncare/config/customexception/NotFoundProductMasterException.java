package org.ateam.oncare.config.customexception;

public class NotFoundProductMasterException extends RuntimeException{
    public NotFoundProductMasterException(String message) {
        super(message);
    }
}
