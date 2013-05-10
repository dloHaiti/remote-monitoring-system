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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != null ? !id.equals(product.id) : product.id != null) return false;
        if (!imageResource.equals(product.imageResource)) return false;
        if (!sku.equals(product.sku)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + sku.hashCode();
        result = 31 * result + imageResource.hashCode();
        return result;
    }
}
