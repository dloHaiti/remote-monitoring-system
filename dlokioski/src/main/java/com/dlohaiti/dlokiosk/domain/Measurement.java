package com.dlohaiti.dlokiosk.domain;

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

    public MeasurementLocation getSamplingSite() {
        return samplingSite;
    }

}
