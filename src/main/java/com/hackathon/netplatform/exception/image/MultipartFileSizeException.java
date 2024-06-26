package com.hackathon.netplatform.exception.image;

public class MultipartFileSizeException extends RuntimeException {
  public MultipartFileSizeException(Integer sizeInMb) {
    super("File size must be up to " + sizeInMb + " MB.");
  }
}
