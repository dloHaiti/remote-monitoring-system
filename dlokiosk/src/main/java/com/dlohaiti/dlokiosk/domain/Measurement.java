package com.dlohaiti.dlokiosk.domain;

import java.math.BigDecimal;

public class Measurement {
    private final String parameterName;
    private final BigDecimal value;

    public Measurement(Parameter parameter, BigDecimal value) {
        this(parameter.getName(), value);
    }

    public Measurement(String parameterName, BigDecimal value) {
        this.parameterName = parameterName;
        this.value = value;
    }

    public String getParameterName() {
        return parameterName;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measurement that = (Measurement) o;

        if (parameterName != null ? !parameterName.equals(that.parameterName) : that.parameterName != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = parameterName != null ? parameterName.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
