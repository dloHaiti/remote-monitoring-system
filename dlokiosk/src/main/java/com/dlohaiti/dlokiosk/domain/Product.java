package com.dlohaiti.dlokiosk.domain;

public class Product {
    private final Long id;
    private final String sku;
    private final int resource;

    public Product(String sku, int resource) {
        this(null, sku, resource);
    }

    public Product(Long id, String sku, int resource) {
        this.id = id;
        this.sku = sku;
        this.resource = resource;
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public int getResource() {
        return resource;
    }
}
