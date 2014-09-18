package com.dlohaiti.dlokiosk.exception;

public class NoCustomerAccountWithGivenIdException extends RuntimeException {
    public NoCustomerAccountWithGivenIdException(long id) {
        super("Unable to find customer account with id: " + id);
    }
}
