package com.dlohaiti.dlokiosk.domain;

public class SamplingSite implements Comparable<SamplingSite> {
    private final String name;

    public SamplingSite(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SamplingSite that = (SamplingSite) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override public int compareTo(SamplingSite another) {
        return name.compareTo(another.name);
    }
}
