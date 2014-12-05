package com.dlohaiti.dlokiosknew.exception;

public class NoProductCategoryWithGivenIdException extends RuntimeException {
    public NoProductCategoryWithGivenIdException(long id) {
        super("Unable to find product category with id: " + id);
    }
}
