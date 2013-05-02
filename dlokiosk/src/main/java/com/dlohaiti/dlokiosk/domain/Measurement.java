package com.dlohaiti.dlokiosk.domain;

public class Measurement {

    private MeasurementType parameter;
    private String value;
    private MeasurementLocation location;

    public Measurement() { }

    public Measurement(MeasurementType parameter, String value, MeasurementLocation location) {
        this.parameter = parameter;
        this.value = value;
        this.location = location;
    }

    public MeasurementType getParameter() {
        return parameter;
    }

    public String getValue() {
        return value;
    }

    public MeasurementLocation getLocation() {
        return location;
    }

    public void setParameter(MeasurementType parameter) {
        this.parameter = parameter;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLocation(MeasurementLocation location) {
        this.location = location;
    }
}
