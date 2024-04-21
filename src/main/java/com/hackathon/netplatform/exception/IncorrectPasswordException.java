package com.hackathon.netplatform.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException() {
        super("Wrong username or password!");
    }
}
