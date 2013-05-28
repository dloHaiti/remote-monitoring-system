package com.dlohaiti.dlokiosk.domain;

public class AppliedPromotion {
    private final String sku;
    private final int quantity;
    private final Money money;

    public AppliedPromotion(String sku, int quantity, Money money) {
        this.sku = sku;
        this.quantity = quantity;
        this.money = money;
    }
}
