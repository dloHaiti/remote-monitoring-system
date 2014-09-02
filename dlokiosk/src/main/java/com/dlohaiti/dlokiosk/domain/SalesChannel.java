package com.dlohaiti.dlokiosk.domain;


import com.dlohaiti.dlokiosk.widgets.SelectableListItem;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.upperCase;

public class SalesChannel extends SelectableListItem implements Comparable<SalesChannel> {
    private long id;
    private String name;
    private String description;

    public SalesChannel(long id, String name, String description) {
        super(id, name);
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    @Override
    public int compareTo(SalesChannel another) {
        return upperCase(defaultString(name)).compareTo(upperCase(defaultString(another.name)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SalesChannel that = (SalesChannel) o;

        if (id != that.id) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SalesChannel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
