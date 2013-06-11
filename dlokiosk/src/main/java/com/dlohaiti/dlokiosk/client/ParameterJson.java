package com.dlohaiti.dlokiosk.client;

import java.math.BigDecimal;
import java.util.List;

public class ParameterJson {
    private boolean isOkNotOk;
    private BigDecimal minimum;
    private BigDecimal maximum;
    private String name;
    private String unit;
    private List<SamplingSiteJson> samplingSites;

    public boolean isOkNotOk() {
        return isOkNotOk;
    }

    public void setIsOkNotOk(boolean okNotOk) {
        isOkNotOk = okNotOk;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<SamplingSiteJson> getSamplingSites() {
        return samplingSites;
    }

    public void setSamplingSites(List<SamplingSiteJson> samplingSites) {
        this.samplingSites = samplingSites;
    }
}
