package com.dlohaiti.dlokiosk.domain.validation;

import com.dlohaiti.dlokiosk.domain.MeasurementType;

import java.util.Set;

public class ValidationResult {
    Set<MeasurementType> invalidMeasurements;

    public ValidationResult(Set<MeasurementType> invalidMeasurements) {
        this.invalidMeasurements = invalidMeasurements;
    }

    public boolean passed() {
        return invalidMeasurements.isEmpty();
    }

    public Set<MeasurementType> getInvalidMeasurements() {
        return invalidMeasurements;
    }
}
