package com.dlohaiti.dlokiosknew;

public class ValidationResult {
    private final boolean hasError;
    private final int errorMessageResourceId;

    public ValidationResult(boolean hasError, int messageResourceId) {
        this.hasError = hasError;
        this.errorMessageResourceId = messageResourceId;
    }

    public ValidationResult(boolean hasError) {
        this.hasError = hasError;
        errorMessageResourceId = 0;
    }

    public boolean hasError() {
        return hasError;
    }

    public int message() {
        return errorMessageResourceId;
    }
}
