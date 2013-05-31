package com.dlohaiti.dlokiosk.domain;

import java.math.BigDecimal;

public class Measurement {
    private final Parameter parameter;
    private final BigDecimal value;

    public Measurement(Parameter parameter, BigDecimal value) {
        this.parameter = parameter;
        this.value = value;
    }

    public String getParameterName() {
        return parameter.getName();
    }

    public BigDecimal getValue() {
        return value;
    }
}
