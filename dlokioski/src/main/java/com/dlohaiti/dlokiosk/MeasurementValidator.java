package com.dlohaiti.dlokiosk;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;

public class MeasurementValidator {
    public boolean validate(Measurement measurement) {
        switch(measurement.getName()) {
            case TEMPERATURE:
                return numberBetweenInclusive(measurement.getValue(), 10, 30);
            case PH:
                return numberBetweenInclusive(measurement.getValue(), 5, 9);
            case TURBIDITY:
                return numberBetweenInclusive(measurement.getValue(), 0, 10);
            case TDS:
                return numberBetweenInclusive(measurement.getValue(), 100, 800);
            case FREE_CHLORINE_CONCENTRATION:
                return numberBetweenInclusive(measurement.getValue(), 5000, 9000);
            case TOTAL_CHLORINE_CONCENTRATION:
                return numberBetweenInclusive(measurement.getValue(), 5000, 10000);
            case FREE_CHLORINE_RESIDUAL:
                return numberBetweenInclusive(measurement.getValue(), 0, 1);
            case TOTAL_CHLORINE_RESIDUAL:
                return numberBetweenInclusive(measurement.getValue(), 0, 1);
            case ALKALINITY:
                return numberBetweenInclusive(measurement.getValue(), 100, 500);
            case HARDNESS:
                return numberBetweenInclusive(measurement.getValue(), 100, 700);
            case COLOR:
                return valueSelected(measurement.getValue());
            case ODOR:
                return valueSelected(measurement.getValue());
            case TASTE:
                return valueSelected(measurement.getValue());
            default:
                return false;
        }
    }

    private boolean valueSelected(String value) {
        if(StringUtils.isNotEmpty(value)) {
            SelectionValue actual = SelectionValue.valueOf(value);
            return actual == SelectionValue.OK || actual == SelectionValue.NOT_OK;
        }
        return false;
    }

    private boolean numberBetweenInclusive(String value, int low, int high) {
        if(NumberUtils.isNumber(value)) {
            BigDecimal actual = new BigDecimal(value);
            return actual.compareTo(new BigDecimal(low)) >= 0 &&
                    actual.compareTo(new BigDecimal(high)) <= 0;
        }
        return false;
    }
}
