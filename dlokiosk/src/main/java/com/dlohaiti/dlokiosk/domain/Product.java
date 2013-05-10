package com.dlohaiti.dlokiosk.domain;

import android.graphics.Bitmap;

public class Product {
    private final Long id;
    private final String sku;
    private final Bitmap imageResource;

    public Product(Long id, String sku, Bitmap resource) {
        this.id = id;
        this.sku = sku;
        this.imageResource = resource;
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public Bitmap getImageResource() {
        return imageResource;
    }
}
