package com.dlohaiti.dlokiosk.domain;

import java.math.BigDecimal;

public class Discount {
    private final String sku;
    private final Money amount;

    public Discount(String sku, Money amount) {
        this.sku = sku;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Discount discount = (Discount) o;

        if (amount != null ? !amount.equals(discount.amount) : discount.amount != null) return false;
        if (sku != null ? !sku.equals(discount.sku) : discount.sku != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sku != null ? sku.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "sku='" + sku + '\'' +
                ", amount=" + amount +
                '}';
    }

    public boolean isFor(Product product) {
        return sku.equals(product.getSku());
    }

    public BigDecimal getAmount() {
        return amount.getAmount();
    }
}
