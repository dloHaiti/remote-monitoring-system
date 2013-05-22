package com.dlohaiti.dlokiosk.domain;

import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("unused")
public class PromotionBuilder {
    private PromotionApplicationType appliesTo = PromotionApplicationType.BASKET;
    private String sku = "ABC";
    private Date startDate = new Date(0);
    private Date endDate = new Date(Long.MAX_VALUE);
    private BigDecimal amount = BigDecimal.TEN;
    private PromotionType type = PromotionType.PERCENT;

    private PromotionBuilder() {}

    public static PromotionBuilder promotionBuilder() {
        return new PromotionBuilder();
    }

    public PromotionBuilder thatAppliesTo(PromotionApplicationType type) {
        this.appliesTo = type;
        return this;
    }

    public PromotionBuilder withSku(String sku) {
        this.sku = sku;
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
        return new Promotion(appliesTo, sku, startDate, endDate, amount.toString(), type);
    }
}
