package com.hackathon.netplatform.exception.image;

import java.util.UUID;

public class ImageNotFoundException extends RuntimeException {
  public ImageNotFoundException(UUID id) {
    super("User with id " + id + " has no attached picture.");
  }
}
