package org.ateam.oncare.config.customexception;

public class InvalidRentalDateException extends RuntimeException {
    public InvalidRentalDateException(String s) {
        super(s);
    }
}
