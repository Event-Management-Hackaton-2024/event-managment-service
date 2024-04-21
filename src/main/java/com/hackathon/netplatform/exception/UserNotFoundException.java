package com.hackathon.netplatform.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("User with email " + email + " not found!");
    }

    public UserNotFoundException(UUID id) {
        super("User with id " + id + " not found");
    }
}
