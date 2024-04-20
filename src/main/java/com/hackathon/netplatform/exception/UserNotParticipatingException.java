package com.hackathon.netplatform.exception;

import java.util.UUID;

public class UserNotParticipatingException extends RuntimeException {
    public UserNotParticipatingException(UUID eventId, UUID userId) {
        super("The user with ID " + userId + " is not participating in the event with ID " + eventId);
    }
}
