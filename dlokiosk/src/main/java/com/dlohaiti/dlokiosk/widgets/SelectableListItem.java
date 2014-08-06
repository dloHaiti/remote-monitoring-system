package com.dlohaiti.dlokiosk.widgets;

public class SelectableListItem {
    private String name;
    private boolean selected;

    public SelectableListItem(String name) {
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
