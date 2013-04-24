package com.dlohaiti.dlokiosk.db;

import com.dlohaiti.dlokiosk.domain.MeasurementType;
import com.dlohaiti.dlokiosk.domain.validation.ValidationResult;

import java.util.Set;

public class SaveResult {
    private final ValidationResult validationResult;
    private final boolean saveSuccessful;

    public SaveResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
        this.saveSuccessful = false;
    }

    public SaveResult(ValidationResult result, boolean saveSuccessful) {
        this.validationResult = result;
        this.saveSuccessful = saveSuccessful;
    }

    public boolean passedValidation() {
        return validationResult.passed();
    }

    public Set<MeasurementType> getValidationFailures() {
        return validationResult.getInvalidMeasurements();
    }

    public boolean successful() {
        return saveSuccessful && validationResult.passed();
    }
}
