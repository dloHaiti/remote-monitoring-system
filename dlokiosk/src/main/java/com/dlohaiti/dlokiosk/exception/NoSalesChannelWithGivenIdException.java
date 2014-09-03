package com.dlohaiti.dlokiosk.exception;

public class NoSalesChannelWithGivenIdException extends RuntimeException {
    public NoSalesChannelWithGivenIdException(long id) {
        super("Unable to find sales channel with id: " + id);
    }
}
