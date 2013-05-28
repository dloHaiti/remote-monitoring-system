package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ReceiptLineItemType;

public class LineItem implements Orderable {
    private final String sku;
    private final Integer quantity;
    private final Money price;
    private final ReceiptLineItemType type;

    public LineItem(String sku, Integer quantity, Money price, ReceiptLineItemType type) {
        this.sku = sku;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
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

    public ReceiptLineItemType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineItem lineItem = (LineItem) o;

        if (price != null ? !price.equals(lineItem.price) : lineItem.price != null) return false;
        if (quantity != null ? !quantity.equals(lineItem.quantity) : lineItem.quantity != null) return false;
        if (sku != null ? !sku.equals(lineItem.sku) : lineItem.sku != null) return false;
        if (type != lineItem.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sku != null ? sku.hashCode() : 0;
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
