package com.hackathon.netplatform.exception;

public class NoAuthenticatedUserException extends RuntimeException {
  public NoAuthenticatedUserException() {
    super("No authenticated user found!");
  }
}
