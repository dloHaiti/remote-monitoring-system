package com.dlohaiti.dlokiosk.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;

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

    public List<Promotion> getAppliedPromotions() {
        return promotions;
    }

    @JsonIgnore public Integer getLineItemsCount() {
        int total = 0;
        for (OrderedProduct op : orderedProducts) {
            total += op.getQuantity();
        }
        return total;
    }

    public Date getCreatedDate() {
        return createdAt;
    }

    //TODO: infer on server side from authentication
    public String getKioskId() {
        return kioskId;
    }

    @JsonIgnore public Long getId() {
        return id;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receipt receipt = (Receipt) o;

        if (createdAt != null ? !createdAt.equals(receipt.createdAt) : receipt.createdAt != null) return false;
        if (id != null ? !id.equals(receipt.id) : receipt.id != null) return false;
        if (kioskId != null ? !kioskId.equals(receipt.kioskId) : receipt.kioskId != null) return false;
        if (orderedProducts != null ? !orderedProducts.equals(receipt.orderedProducts) : receipt.orderedProducts != null)
            return false;
        if (promotions != null ? !promotions.equals(receipt.promotions) : receipt.promotions != null) return false;

        return true;
    }

    @Override public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (orderedProducts != null ? orderedProducts.hashCode() : 0);
        result = 31 * result + (promotions != null ? promotions.hashCode() : 0);
        result = 31 * result + (kioskId != null ? kioskId.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }
}
