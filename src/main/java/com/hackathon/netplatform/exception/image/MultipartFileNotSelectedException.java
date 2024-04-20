package com.hackathon.netplatform.exception.image;

public class MultipartFileNotSelectedException extends RuntimeException{
    public MultipartFileNotSelectedException() {
        super("Please select a file!");
    }
}
