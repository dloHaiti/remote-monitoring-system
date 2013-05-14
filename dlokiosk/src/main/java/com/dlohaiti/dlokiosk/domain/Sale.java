package com.dlohaiti.dlokiosk.domain;

import java.util.Date;

public class Sale {
    private final Long id;
    private final String sku;
    private final Integer quantity;
    private final Date createdAt;

    public Sale(Long id, String sku, Integer quantity, Date createdAt) {
        this.id = id;
        this.sku = sku;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public String getKiosk() {
        return "HARDCODED K1";
    }

    public String getSku() {
        return sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Date getTimestamp() {
        return createdAt;
    }
}
