package com.hackathon.netplatform.exception.image;

public class MultipartFileContentTypeException extends RuntimeException{
    public MultipartFileContentTypeException() {
        super("Only PNG or JPG images are allowed.");
    }
}
