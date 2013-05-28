package com.dlohaiti.dlokiosk.domain;

public class OrderedProduct implements Orderable {
    private final String sku;
    private final Integer quantity;
    private final Money price;

    public OrderedProduct(String sku, Integer quantity, Money price) {
        this.sku = sku;
        this.quantity = quantity;
        this.price = price;
    }

    @Override public String getSku() {
        return sku;
    }

    @Override public Integer getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderedProduct product = (OrderedProduct) o;

        if (price != null ? !price.equals(product.price) : product.price != null) return false;
        if (quantity != null ? !quantity.equals(product.quantity) : product.quantity != null) return false;
        if (sku != null ? !sku.equals(product.sku) : product.sku != null) return false;

        return true;
    }

    @Override public int hashCode() {
        int result = sku != null ? sku.hashCode() : 0;
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
