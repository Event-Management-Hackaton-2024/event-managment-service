package com.hackathon.netplatform.exception;

public class InterestNotFoundException extends RuntimeException {
    public InterestNotFoundException(String name) {
        super("Interest with name " + name + " not found!");
    }
}
