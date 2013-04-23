package com.dlohaiti.dlokiosk;

public class Measurement {

    private MeasurementType name;
    private String value;
    private MeasurementLocation samplingSite;

    public Measurement(MeasurementType name, String value, MeasurementLocation samplingSite) {
        this.name = name;
        this.value = value;
        this.samplingSite = samplingSite;
    }

    public MeasurementType getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSamplingSite() {
        return samplingSite.name();
    }

}
