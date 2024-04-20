package com.hackathon.netplatform.exception;

public class InterestExistsException extends RuntimeException {
    public InterestExistsException(String name) {
        super("Interest with name " + name + " already exists!");
    }
}
