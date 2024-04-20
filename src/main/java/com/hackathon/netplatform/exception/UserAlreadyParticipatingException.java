package com.hackathon.netplatform.exception;

import java.util.UUID;

public class UserAlreadyParticipatingException extends RuntimeException {
    public UserAlreadyParticipatingException(UUID eventId,UUID userId) {
        super("The user with ID " + userId + " is already participating in the event with ID " + eventId);
    }
}
