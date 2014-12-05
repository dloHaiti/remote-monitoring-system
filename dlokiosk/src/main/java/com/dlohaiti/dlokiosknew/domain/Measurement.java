package com.dlohaiti.dlokiosknew.domain;

import java.math.BigDecimal;

public class Measurement {
    private Long id;
    private final String parameterName;
    private BigDecimal value;

    public Measurement(Long id, String parameterName, BigDecimal value) {
        this.id = id;
        this.parameterName = parameterName;
        this.value = value;
    }

    public Measurement(Parameter parameter, BigDecimal value) {
        this(null,parameter.getName(), value);
    }

    public Measurement(String parameterName, BigDecimal value) {
        this(null,parameterName, value);
    }

    public String getParameterName() {
        return parameterName;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Measurement that = (Measurement) o;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (parameterName != null ? !parameterName.equals(that.parameterName) : that.parameterName != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + parameterName != null ? parameterName.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }
}
