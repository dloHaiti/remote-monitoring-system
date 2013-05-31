package com.dlohaiti.dlokiosk.domain;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.upperCase;

public class SamplingSite implements Comparable<SamplingSite> {
    private final Integer id;
    private final String name;

    public SamplingSite(String name) {
        this(null, name);
    }

    public SamplingSite(Integer id, String name) {
        this.id = id;
        this.name = defaultString(name);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SamplingSite that = (SamplingSite) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override public int compareTo(SamplingSite another) {
        return upperCase(name).compareTo(upperCase(another.name));
    }
}
