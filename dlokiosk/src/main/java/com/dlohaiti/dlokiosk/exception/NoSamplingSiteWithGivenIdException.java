package com.dlohaiti.dlokiosk.exception;

public class NoSamplingSiteWithGivenIdException extends RuntimeException {
    public NoSamplingSiteWithGivenIdException(long id) {
        super("Unable to find Sampling site with id: " + id);
    }
}
