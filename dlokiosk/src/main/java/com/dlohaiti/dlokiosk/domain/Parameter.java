package com.dlohaiti.dlokiosk.domain;

import java.math.BigDecimal;

public class Parameter implements Comparable<Parameter> {
    private final String name;
    private final String unitOfMeasure;
    private final BigDecimal minimum;
    private final BigDecimal maximum;

    public Parameter(String name, String unitOfMeasure, String minimum, String maximum) {
        this.name = name;
        this.unitOfMeasure = unitOfMeasure;
        this.minimum = new BigDecimal(minimum);
        this.maximum = new BigDecimal(maximum);
    }

    @Override public int compareTo(Parameter parameter) {
        return name.compareTo(parameter.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parameter parameter = (Parameter) o;

        if (maximum != null ? !maximum.equals(parameter.maximum) : parameter.maximum != null) return false;
        if (minimum != null ? !minimum.equals(parameter.minimum) : parameter.minimum != null) return false;
        if (name != null ? !name.equals(parameter.name) : parameter.name != null) return false;
        if (unitOfMeasure != null ? !unitOfMeasure.equals(parameter.unitOfMeasure) : parameter.unitOfMeasure != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (unitOfMeasure != null ? unitOfMeasure.hashCode() : 0);
        result = 31 * result + (minimum != null ? minimum.hashCode() : 0);
        result = 31 * result + (maximum != null ? maximum.hashCode() : 0);
        return result;
    }
}
