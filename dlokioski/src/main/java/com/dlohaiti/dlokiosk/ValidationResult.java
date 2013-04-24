package com.dlohaiti.dlokiosk;

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
