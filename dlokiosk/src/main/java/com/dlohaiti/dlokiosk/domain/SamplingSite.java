package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.SelectableListItem;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.upperCase;

public class SamplingSite implements Comparable<SamplingSite> {
    private final Long id;
    private final String name;
    private boolean selected;

    public SamplingSite(String name) {
        this(null, name);
    }

    public SamplingSite(Long id, String name) {
        this.id = id;
        this.name = defaultString(name);
    }

    public Long getId() {
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
    public boolean isSelected() {
        return selected;
    }

    public SamplingSite select() {
        this.selected = true;
        return this;
    }

    public SamplingSite unSelect() {
        this.selected = false;
        return this;
    }
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public int compareTo(SamplingSite another) {
        return upperCase(name).compareTo(upperCase(another.name));
    }
}
