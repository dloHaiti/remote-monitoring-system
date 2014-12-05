package com.dlohaiti.dlokiosknew.client;

import java.util.List;

public class ParameterJson {
    private boolean isOkNotOk;
    private boolean isUsedInTotalizer;
    private String minimum;
    private String maximum;
    private String name;
    private String unit;
    private Integer priority;
    private List<SamplingSiteJson> samplingSites;

    public boolean isUsedInTotalizer() {
        return isUsedInTotalizer;
    }

    public void setIsUsedInTotalizer(boolean isUsedInTotalizer) {
        this.isUsedInTotalizer = isUsedInTotalizer;
    }

    public boolean isOkNotOk() {
        return isOkNotOk;
    }

    public void setIsOkNotOk(boolean okNotOk) {
        isOkNotOk = okNotOk;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<SamplingSiteJson> getSamplingSites() {
        return samplingSites;
    }

    public void setSamplingSites(List<SamplingSiteJson> samplingSites) {
        this.samplingSites = samplingSites;
    }
}
