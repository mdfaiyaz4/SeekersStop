package com.faiyaz.SeekersStop.UserDefinedExceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
