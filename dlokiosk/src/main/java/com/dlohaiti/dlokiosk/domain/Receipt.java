package com.dlohaiti.dlokiosk.domain;

import java.util.*;

public class Receipt {
    private final Long id;
    private final List<OrderedProduct> orderedProducts;
    private final List<Promotion> promotions;
    private final String kioskId;
    private final Date createdAt;

    public Receipt(List<OrderedProduct> orderedProducts, List<Promotion> promotions, String kioskId, Date createdAt) {
        this(null, orderedProducts, promotions, kioskId, createdAt);
    }

    public Receipt(Long id, List<OrderedProduct> orderedProducts, List<Promotion> promotions, String kioskId, Date createdAt) {
        this.id = id;
        this.orderedProducts = orderedProducts;
        this.promotions = promotions;
        this.kioskId = kioskId;
        this.createdAt = createdAt;
    }

    public List<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public Integer getLineItemsCount() {
        int total = 0;
        for (OrderedProduct op : orderedProducts) {
            total += op.getQuantity();
        }
        return total;
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
