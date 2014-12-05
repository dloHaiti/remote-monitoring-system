package com.dlohaiti.dlokiosknew.exception;

public class NoCustomerAccountWithGivenIdException extends RuntimeException {
    public NoCustomerAccountWithGivenIdException(String id) {
        super("Unable to find customer account with id: " + id);
    }
}
