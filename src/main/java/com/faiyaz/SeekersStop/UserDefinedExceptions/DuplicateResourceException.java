package com.faiyaz.SeekersStop.UserDefinedExceptions;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
