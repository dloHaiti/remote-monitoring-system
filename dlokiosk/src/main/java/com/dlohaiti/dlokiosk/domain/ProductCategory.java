package com.dlohaiti.dlokiosk.domain;

import android.graphics.Bitmap;
import com.dlohaiti.dlokiosk.SelectableListItem;

public class ProductCategory extends SelectableListItem {
    private long id;
    private String name;
    private Bitmap icon;

    public ProductCategory(long id, String name, Bitmap icon) {
        super(id, name);
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Bitmap icon() {
        return icon;
    }
}
