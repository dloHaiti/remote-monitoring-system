package com.dlohaiti.dlokiosk.domain;

public class SampleSite implements Comparable<SampleSite> {
    private final String name;

    public SampleSite(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SampleSite that = (SampleSite) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override public int compareTo(SampleSite another) {
        return name.compareTo(another.name);
    }
}
