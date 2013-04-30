package com.dlohaiti.dlokiosk.domain.validation;

import com.dlohaiti.dlokiosk.domain.Measurement;
import com.dlohaiti.dlokiosk.domain.MeasurementType;
import com.google.inject.Inject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MeasurementsValidator {
    private MeasurementValidator validator;

    @Inject
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
