package com.dlohaiti.dlokiosk;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;

public class MeasurementValidator {
    public boolean validate(Measurement measurement) {
        switch(measurement.getName()) {
            case TEMPERATURE:
                return locationSelectedAndNumberBetweenInclusive(measurement, 10, 30);
            case PH:
                return locationSelectedAndNumberBetweenInclusive(measurement, 5, 9);
            case TURBIDITY:
                return locationSelectedAndNumberBetweenInclusive(measurement, 0, 10);
            case TDS:
                return locationSelectedAndNumberBetweenInclusive(measurement, 100, 800);
            case FREE_CHLORINE_CONCENTRATION:
                return locationSelectedAndNumberBetweenInclusive(measurement, 5000, 9000);
            case TOTAL_CHLORINE_CONCENTRATION:
                return locationSelectedAndNumberBetweenInclusive(measurement, 5000, 10000);
            case FREE_CHLORINE_RESIDUAL:
                return locationSelectedAndNumberBetweenInclusive(measurement, 0, 1);
            case TOTAL_CHLORINE_RESIDUAL:
                return locationSelectedAndNumberBetweenInclusive(measurement, 0, 1);
            case ALKALINITY:
                return locationSelectedAndNumberBetweenInclusive(measurement, 100, 500);
            case HARDNESS:
                return locationSelectedAndNumberBetweenInclusive(measurement, 100, 700);
            case COLOR:
                return locationAndValueSelected(measurement);
            case ODOR:
                return locationAndValueSelected(measurement);
            case TASTE:
                return locationAndValueSelected(measurement);
            default:
                return false;
        }
    }

    private boolean locationSelected(MeasurementLocation samplingSite) {
        return MeasurementLocation.UNSELECTED != samplingSite;
    }

    private boolean locationAndValueSelected(Measurement measurement) {
        boolean locationSelected = locationSelected(measurement.getSamplingSite());
        if(StringUtils.isNotEmpty(measurement.getValue())) {
            SelectionValue actual = SelectionValue.valueOf(measurement.getValue());
            return locationSelected && actual != SelectionValue.UNSELECTED;
        }
        return false;
    }

    private boolean locationSelectedAndNumberBetweenInclusive(Measurement measurement, int low, int high) {
        boolean locationSelected = locationSelected(measurement.getSamplingSite());
        if(NumberUtils.isNumber(measurement.getValue())) {
            BigDecimal actual = new BigDecimal(measurement.getValue());
            return locationSelected &&
                    actual.compareTo(new BigDecimal(low)) >= 0 &&
                    actual.compareTo(new BigDecimal(high)) <= 0;
        }
        return false;
    }
}
