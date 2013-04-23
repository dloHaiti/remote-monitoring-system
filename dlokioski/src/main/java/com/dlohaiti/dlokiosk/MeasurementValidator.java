package com.dlohaiti.dlokiosk;

public class MeasurementValidator {
    public boolean validate(Measurement measurement) {
        switch(measurement.getName()) {
            case TEMPERATURE:
                Double degrees = Double.valueOf(measurement.getValue());
                return degrees >= 10 && degrees <= 30;
            case PH:
                Double pH = Double.valueOf(measurement.getValue());
                return pH >= 5 && pH <= 9;
            case TURBIDITY:
                Double turbidity = Double.valueOf(measurement.getValue());
                return turbidity >= 0 && turbidity <= 10;
            case TDS:
                Double tds = Double.valueOf(measurement.getValue());
                return tds >= 100 && tds <= 800;
            case FREE_CHLORINE_CONCENTRATION:
                Double freeChlorineConcentration = Double.valueOf(measurement.getValue());
                return freeChlorineConcentration >= 5000 && freeChlorineConcentration <= 9000;
            case TOTAL_CHLORINE_CONCENTRATION:
                Double totalChlorineConcentration = Double.valueOf(measurement.getValue());
                return totalChlorineConcentration >= 5000 && totalChlorineConcentration <= 10000;
            case FREE_CHLORINE_RESIDUAL:
                Double freeChlorineResidual = Double.valueOf(measurement.getValue());
                return freeChlorineResidual >= 0 && freeChlorineResidual <= 1;
            case TOTAL_CHLORINE_RESIDUAL:
                Double totalChlorineResidual = Double.valueOf(measurement.getValue());
                return totalChlorineResidual >= 0 && totalChlorineResidual <= 1;
            case ALKALINITY:
                Double alkalinity = Double.valueOf(measurement.getValue());
                return alkalinity >= 100 && alkalinity <= 500;
            case HARDNESS:
                Double hardness = Double.valueOf(measurement.getValue());
                return hardness >= 100 && hardness <= 700;
            case COLOR:

            default:
                return false;
        }
    }
}
