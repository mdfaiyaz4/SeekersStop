package com.faiyaz.SeekersStop.UserDefinedExceptions;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
}
