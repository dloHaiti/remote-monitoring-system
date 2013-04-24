package com.dlohaiti.dlokiosk;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MeasurementsValidatorTest {

    private MeasurementsValidator validator;

    @Before
    public void setUp() {
        validator = new MeasurementsValidator(new MeasurementValidator());
    }

    @Test
    public void shouldReturnTheMeasurementsThatFailedValidation() {
        List<Measurement> measurements = new ArrayList<Measurement>();
        // invalid value
        measurements.add(new Measurement(MeasurementType.TEMPERATURE, "-40", MeasurementLocation.BOREHOLE));
        // invalid location
        measurements.add(new Measurement(MeasurementType.PH, "8", MeasurementLocation.UNSELECTED));
        // valid
        measurements.add(new Measurement(MeasurementType.COLOR, "OK", MeasurementLocation.WTU_EFF));

        ValidationResult validationResult = validator.validate(measurements);

        assertThat(validationResult.passed(), is(false));
        Set<MeasurementType> invalidMeasurements = new HashSet<MeasurementType>();
        invalidMeasurements.add(MeasurementType.TEMPERATURE);
        invalidMeasurements.add(MeasurementType.PH);
        assertThat(validationResult.getInvalidMeasurements(), is(invalidMeasurements));
    }
}
