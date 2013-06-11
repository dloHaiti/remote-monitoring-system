package com.dlohaiti.dlokiosk.client;

import java.math.BigDecimal;

public class PromotionJson {
    private String appliesTo;
    private String startDate;
    private String endDate;
    private String productSku;
    private BigDecimal amount;
    private String type;
    private String sku;
    private boolean hasRange;

    public String getAppliesTo() {
        return appliesTo;
    }

    public void setAppliesTo(String appliesTo) {
        this.appliesTo = appliesTo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public boolean isHasRange() {
        return hasRange;
    }

    public void setHasRange(boolean hasRange) {
        this.hasRange = hasRange;
    }
}
