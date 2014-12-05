package com.dlohaiti.dlokiosknew.domain;

import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("unused")
public class PromotionBuilder {
    private PromotionApplicationType appliesTo = PromotionApplicationType.BASKET;
    private String productSku = "ABC";
    private Date startDate = new Date(0);
    private Date endDate = new Date(Long.MAX_VALUE);
    private BigDecimal amount = BigDecimal.TEN;
    private PromotionType type = PromotionType.PERCENT;
    private String sku = "PROMO10";

    private PromotionBuilder() {
    }

    public static PromotionBuilder promotionBuilder() {
        return new PromotionBuilder();
    }

    public PromotionBuilder withSku(String sku) {
        this.sku = sku;
        return this;
    }

    public PromotionBuilder thatAppliesTo(PromotionApplicationType type) {
        this.appliesTo = type;
        return this;
    }

    public PromotionBuilder withProductSku(String productSku) {
        this.productSku = productSku;
        return this;
    }

    public PromotionBuilder withStartDate(Date start) {
        this.startDate = start;
        return this;
    }

    public PromotionBuilder withEndDate(Date end) {
        this.endDate = end;
        return this;
    }

    public PromotionBuilder withAmount(String amount) {
        this.amount = new BigDecimal(amount);
        return this;
    }

    public PromotionBuilder withPromotionType(PromotionType type) {
        this.type = type;
        return this;
    }

    public Promotion build() {
        return new Promotion(null, sku, appliesTo, productSku, startDate, endDate, amount.toString(), type, null);
    }
}
