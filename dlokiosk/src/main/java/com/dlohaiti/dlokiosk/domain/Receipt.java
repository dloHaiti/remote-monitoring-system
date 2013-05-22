package com.dlohaiti.dlokiosk.domain;

import java.util.*;

public class Receipt {
    private final Long id;
    private final List<OrderedProduct> orderedProducts;
    private final String kioskId;
    private final Date createdAt;

    public Receipt(List<OrderedProduct> orderedProducts, String kioskId, Date createdAt) {
        this(null, orderedProducts, kioskId, createdAt);
    }

    public Receipt(Long id, List<OrderedProduct> orderedProducts, String kioskId, Date createdAt) {
        this.id = id;
        this.orderedProducts = orderedProducts;
        this.kioskId = kioskId;
        this.createdAt = createdAt;
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public Integer getLineItemsCount() {
        int total = 0;
        for (OrderedProduct op : orderedProducts) {
            total += op.getQuantity();
        }
        return total;
    }

    public Integer getTotalProducts() {
        return orderedProducts.size();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getKioskId() {
        return kioskId;
    }

    public Long getId() {
        return id;
    }
}
