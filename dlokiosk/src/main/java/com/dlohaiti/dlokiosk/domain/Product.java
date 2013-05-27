package com.dlohaiti.dlokiosk.domain;

import android.graphics.Bitmap;
import com.dlohaiti.dlokiosk.VisibleGridItem;

public class Product implements VisibleGridItem {
    //TODO: needs description
    //TODO: needs gallons
    private final Long id;
    private final String sku;
    private final Bitmap imageResource;
    private final boolean requiresQuantity;
    private final Integer quantity;
    private final Integer minimumQuantity;
    private final Integer maximumQuantity;
    private final Money price;

    public Product(Long id, String sku, Bitmap imageResource, boolean requiresQuantity, Integer quantity, Integer minimumQuantity, Integer maximumQuantity, Money price) {
        this.id = id;
        this.sku = sku;
        this.imageResource = imageResource;
        this.requiresQuantity = requiresQuantity;
        this.quantity = quantity;
        this.minimumQuantity = minimumQuantity;
        this.maximumQuantity = maximumQuantity;
        this.price = price;
    }

    public Product withQuantity(int quantity) {
        return new Product(id, sku, imageResource, requiresQuantity, quantity, minimumQuantity, maximumQuantity, price);
    }

    @Override public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    @Override public Bitmap getImageResource() {
        return imageResource;
    }

    public boolean requiresQuantity() {
        return requiresQuantity;
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

    public Money getPrice() {
        return price;
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
        if (maximumQuantity != null ? !maximumQuantity.equals(product.maximumQuantity) : product.maximumQuantity != null)
            return false;
        if (minimumQuantity != null ? !minimumQuantity.equals(product.minimumQuantity) : product.minimumQuantity != null)
            return false;
        if (price != null ? !price.equals(product.price) : product.price != null) return false;
        if (quantity != null ? !quantity.equals(product.quantity) : product.quantity != null) return false;
        if (sku != null ? !sku.equals(product.sku) : product.sku != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sku != null ? sku.hashCode() : 0);
        result = 31 * result + (imageResource != null ? imageResource.hashCode() : 0);
        result = 31 * result + (requiresQuantity ? 1 : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (minimumQuantity != null ? minimumQuantity.hashCode() : 0);
        result = 31 * result + (maximumQuantity != null ? maximumQuantity.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
