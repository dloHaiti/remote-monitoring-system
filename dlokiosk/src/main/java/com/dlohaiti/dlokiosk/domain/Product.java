package com.dlohaiti.dlokiosk.domain;

public class Product {
    private final Long id;
    private final String sku;

    public Product(String sku) {
        this(null, sku);
    }

    public Product(Long id, String sku) {
        this.id = id;
        this.sku = sku;
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }
}
