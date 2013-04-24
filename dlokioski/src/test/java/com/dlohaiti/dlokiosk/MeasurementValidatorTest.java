package com.dlohaiti.dlokiosk;

import org.junit.Before;
import org.junit.Test;

import static com.dlohaiti.dlokiosk.MeasurementLocation.BOREHOLE;
import static com.dlohaiti.dlokiosk.MeasurementLocation.UNSELECTED;
import static com.dlohaiti.dlokiosk.MeasurementType.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MeasurementValidatorTest {

    private MeasurementValidator validator;

    private void expectValid(MeasurementType measurementType, String... values) {
        for (String value : values) {
            Measurement measurement = new Measurement(measurementType, value, BOREHOLE);
            boolean passed = validator.validate(measurement);
            assertThat(passed, is(true));
        }
    }

    private void expectInvalidLocation(MeasurementType type, String value, MeasurementLocation location) {
        Measurement measurement = new Measurement(type, value, location);
        boolean passed = validator.validate(measurement);
        assertThat(passed, is(false));
    }

    private void expectInvalid(MeasurementType measurementType, String... values) {
        for (String value : values) {
            Measurement measurement = new Measurement(measurementType, value, BOREHOLE);
            boolean passed = validator.validate(measurement);
            assertThat(passed, is(false));
        }
    }

    @Before
    public void setUp() {
        validator = new MeasurementValidator();
    }

    @Test
    public void shouldValidateTemperature() {
        expectInvalidLocation(TEMPERATURE, "20", UNSELECTED);
        expectInvalid(TEMPERATURE, "9.9", "30.1", "");
        expectValid(TEMPERATURE, "10", "15", "30");
    }

    @Test
    public void shouldValidatePh() {
        expectInvalidLocation(PH, "8", UNSELECTED);
        expectInvalid(PH, "4.9", "9.1", "");
        expectValid(PH, "5", "7", "9");
    }

    @Test
    public void shouldValidateTurbidity() {
        expectInvalidLocation(TURBIDITY, "7", UNSELECTED);
        expectInvalid(TURBIDITY, "-0.1", "10.1", "");
        expectValid(TURBIDITY, "0", "5.5", "10");
    }

    @Test
    public void shouldValidateTds() {
        expectInvalidLocation(TDS, "300", UNSELECTED);
        expectInvalid(TDS, "99.9", "800.1", "");
        expectValid(TDS, "100", "500", "800");
    }

    @Test
    public void shouldValidateFreeChlorineConcentration() {
        expectInvalidLocation(FREE_CHLORINE_CONCENTRATION, "6000", UNSELECTED);
        expectInvalid(FREE_CHLORINE_CONCENTRATION, "4999.9", "9000.1", "");
        expectValid(FREE_CHLORINE_CONCENTRATION, "5000", "7000", "9000");
    }

    @Test
    public void shouldValidateTotalChlorineConcentration() {
        expectInvalidLocation(TOTAL_CHLORINE_CONCENTRATION, "6000", UNSELECTED);
        expectInvalid(TOTAL_CHLORINE_CONCENTRATION, "4999.9", "10000.1", "");
        expectValid(TOTAL_CHLORINE_CONCENTRATION, "5000", "8000", "10000");
    }

    @Test
    public void shouldValidateFreeChlorineResidual() {
        expectInvalidLocation(TOTAL_CHLORINE_CONCENTRATION, "0.8", UNSELECTED);
        expectInvalid(FREE_CHLORINE_RESIDUAL, "-0.1", "1.1", "");
        expectValid(FREE_CHLORINE_RESIDUAL, "0", "0.4", "1");
    }

    @Test
    public void shouldValidateTotalChlorineResidual() {
        expectInvalidLocation(TOTAL_CHLORINE_RESIDUAL, "0.5", UNSELECTED);
        expectInvalid(TOTAL_CHLORINE_RESIDUAL, "-0.1", "1.1", "");
        expectValid(TOTAL_CHLORINE_RESIDUAL, "0", "0.4", "1");
    }

    @Test
    public void shouldValidateAlkalinity() {
        expectInvalidLocation(ALKALINITY, "200", UNSELECTED);
        expectInvalid(ALKALINITY, "99.9", "500.1", "");
        expectValid(ALKALINITY, "100", "250", "500");
    }

    @Test
    public void shouldValidateHardness() {
        expectInvalidLocation(HARDNESS, "650", UNSELECTED);
        expectInvalid(HARDNESS, "99.9", "700.1", "");
        expectValid(HARDNESS, "100", "375", "700");
    }

    @Test
    public void shouldValidateColor() {
        expectInvalidLocation(COLOR, "OK", UNSELECTED);
        expectInvalid(COLOR, "UNSELECTED", "");
        expectValid(COLOR, "OK", "NOT_OK");
    }

    @Test
    public void shouldValidateOdor() {
        expectInvalidLocation(ODOR, "OK", UNSELECTED);
        expectInvalid(ODOR, "UNSELECTED", "");
        expectValid(ODOR, "OK", "NOT_OK");
    }

    @Test
    public void shouldValidateTaste() {
        expectInvalidLocation(TASTE, "OK", UNSELECTED);
        expectInvalid(TASTE, "UNSELECTED", "");
        expectValid(TASTE, "OK", "NOT_OK");
    }
}
