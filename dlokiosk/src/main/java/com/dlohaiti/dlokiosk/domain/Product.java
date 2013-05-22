package com.dlohaiti.dlokiosk.domain;

import android.graphics.Bitmap;

public class Product {
    private final Long id;
    private final String sku;
    private final Bitmap imageResource;
    private final boolean requiresQuantity;
    private final int quantity;

    public Product(Long id, String sku, Bitmap resource) {
        this(id, sku, resource, false, 1);
    }

    public Product(Long id, String sku, Bitmap resource, boolean requiresQuantity) {
        this(id, sku, resource, requiresQuantity, 1);
    }

    public Product(Long id, String sku, Bitmap imageResource, boolean requiresQuantity, int quantity) {
        this.id = id;
        this.sku = sku;
        this.imageResource = imageResource;
        this.requiresQuantity = requiresQuantity;
        this.quantity = quantity;
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

    public boolean requiresQuantity() {
        return requiresQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (requiresQuantity != product.requiresQuantity) return false;
        if (id != null ? !id.equals(product.id) : product.id != null) return false;
        if (imageResource != null ? !imageResource.equals(product.imageResource) : product.imageResource != null)
            return false;
        if (sku != null ? !sku.equals(product.sku) : product.sku != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sku != null ? sku.hashCode() : 0);
        result = 31 * result + (imageResource != null ? imageResource.hashCode() : 0);
        result = 31 * result + (requiresQuantity ? 1 : 0);
        return result;
    }

    public Product withQuantity(int quantity) {
        return new Product(id, sku, imageResource, requiresQuantity, quantity);
    }

    public int getQuantity() {
        return quantity;
    }
}
