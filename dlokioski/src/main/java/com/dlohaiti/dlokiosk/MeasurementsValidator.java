package com.dlohaiti.dlokiosk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MeasurementsValidator {
    private MeasurementValidator validator;

    public MeasurementsValidator(MeasurementValidator validator) {
        this.validator = validator;
    }

    public ValidationResult validate(List<Measurement> measurements) {
        Set<MeasurementType> invalidMeasurements = new HashSet<MeasurementType>();
        for(Measurement measurement : measurements) {
            boolean passed = validator.validate(measurement);
            if(!passed) {
                invalidMeasurements.add(measurement.getName());
            }
        }
        return new ValidationResult(invalidMeasurements);
    }
}
