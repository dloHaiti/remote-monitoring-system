package com.dlohaiti.dlokiosknew.domain;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.upperCase;

public class DeliveryAgent implements Comparable<DeliveryAgent> {
    private final String name;

    public DeliveryAgent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeliveryAgent that = (DeliveryAgent) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(DeliveryAgent another) {
        return upperCase(defaultString(name)).compareTo(upperCase(defaultString(another.name)));
    }
}
