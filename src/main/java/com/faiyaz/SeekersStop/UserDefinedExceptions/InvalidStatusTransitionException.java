package com.faiyaz.SeekersStop.UserDefinedExceptions;

import com.faiyaz.SeekersStop.Enums.ApplicationStatus;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(ApplicationStatus currentStatus, ApplicationStatus requestedStatus) {
        super("Invalid application status transition from " + currentStatus + " to " + requestedStatus);
    }
}
