package com.hackathon.netplatform.exception;

import java.util.UUID;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(UUID id) {
        super("Event with id " + id + " not found!");
    }
}
