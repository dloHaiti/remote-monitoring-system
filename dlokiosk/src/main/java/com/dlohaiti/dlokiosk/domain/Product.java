package com.dlohaiti.dlokiosk.domain;

import android.graphics.Bitmap;

public class Product {
    private final Long id;
    private final String sku;
    private final Bitmap imageResource;
    private final boolean requiresQuantity;
    private final Integer quantity;
    private final Integer minimumQuantity;
    private final Integer maximumQuantity;

    public Product(Long id, String sku, Bitmap resource) {
        this(id, sku, resource, false, 1, null, null);
    }

    public Product(Long id, String sku, Bitmap resource, boolean requiresQuantity) {
        this(id, sku, resource, requiresQuantity, 1, null, null);
    }

    public Product(Long id, String sku, Bitmap imageResource, boolean requiresQuantity, Integer quantity, Integer minimumQuantity, Integer maximumQuantity) {
        this.id = id;
        this.sku = sku;
        this.imageResource = imageResource;
        this.requiresQuantity = requiresQuantity;
        this.quantity = quantity;
        this.minimumQuantity = minimumQuantity;
        this.maximumQuantity = maximumQuantity;
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

    public Product withQuantity(int quantity) {
        return new Product(id, sku, imageResource, requiresQuantity, quantity, minimumQuantity, maximumQuantity);
    }

    public int getQuantity() {
        return quantity;
    }

    public Integer getMinimumQuantity() {
        return minimumQuantity;
    }

    public Integer getMaximumQuantity() {
        return maximumQuantity;
    }
}
