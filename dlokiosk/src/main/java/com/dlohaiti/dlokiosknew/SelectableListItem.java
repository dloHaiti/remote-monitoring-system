package com.dlohaiti.dlokiosknew;

public class SelectableListItem {
    private long id;
    private String name;
    private boolean selected;

    public SelectableListItem(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String name() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public SelectableListItem select() {
        this.selected = true;
        return this;
    }

    public SelectableListItem unSelect() {
        this.selected = false;
        return this;
    }
}
