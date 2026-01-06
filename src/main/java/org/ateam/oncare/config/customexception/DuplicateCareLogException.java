package org.ateam.oncare.config.customexception;

public class DuplicateCareLogException extends RuntimeException {

    public DuplicateCareLogException(String message) {
        super(message);
    }
}
