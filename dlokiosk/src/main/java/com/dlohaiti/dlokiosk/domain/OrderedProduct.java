package com.dlohaiti.dlokiosk.domain;

public class OrderedProduct implements Orderable {
    //TODO: needs gallons
    //TODO: needs total price
    private final String sku;
    private final Integer quantity;

    public OrderedProduct(String sku, Integer quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    @Override public String getSku() {
        return sku;
    }

    @Override public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderedProduct that = (OrderedProduct) o;

        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;
        if (sku != null ? !sku.equals(that.sku) : that.sku != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sku != null ? sku.hashCode() : 0;
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }
}
