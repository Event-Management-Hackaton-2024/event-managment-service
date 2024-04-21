package com.hackathon.netplatform.exception;

import java.util.UUID;

public class InterestAlreadyUsedException extends RuntimeException {
  public InterestAlreadyUsedException(UUID id) {
    super("The interest with id" + id + " is already used");
  }
}
