package com.dlohaiti.dlokiosk.domain;

import android.graphics.Bitmap;

@SuppressWarnings("unused")
public class ProductBuilder {
    private Long id = 1L;
    private String sku = "ABC";
    private Bitmap resource = null;
    private boolean requiresQuantity = false;
    private Integer quantity = 1;
    private Integer minimum = null;
    private Integer maximum = null;
    private Money price = new Money(1.0, "HTG");

    private ProductBuilder(){}

    public static ProductBuilder productBuilder() {
        return new ProductBuilder();
    }

    public ProductBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ProductBuilder withSku(String sku) {
        this.sku = sku;
        return this;
    }

    public ProductBuilder withResource(Bitmap resource) {
        this.resource = resource;
        return this;
    }

    public ProductBuilder thatRequiresQuantity() {
        this.requiresQuantity = true;
        return this;
    }

    public ProductBuilder thatDoesNotRequireQuantity() {
        this.requiresQuantity = false;
        return this;
    }

    public ProductBuilder withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public ProductBuilder withMinimumQuantity(Integer minimum) {
        this.minimum = minimum;
        return this;
    }

    public ProductBuilder withMaximumQuantity(Integer maximum) {
        this.maximum = maximum;
        return this;
    }

    public ProductBuilder withPrice(Double amount, String currencyCode) {
        this.price = new Money(amount, currencyCode);
        return this;
    }

    public Product build() {
        return new Product(id, sku, resource, requiresQuantity, quantity, minimum, maximum, price);
    }
}
