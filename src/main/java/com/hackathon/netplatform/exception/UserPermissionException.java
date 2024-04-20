package com.hackathon.netplatform.exception;

public class UserPermissionException extends RuntimeException {
    public UserPermissionException() {
        super("You have no permission to edit this user!");
    }
}
