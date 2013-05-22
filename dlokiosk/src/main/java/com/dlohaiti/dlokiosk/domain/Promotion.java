package com.dlohaiti.dlokiosk.domain;

import android.graphics.Bitmap;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Promotion {
    private final PromotionApplicationType appliesTo;
    private final String sku;
    private final Date startDate;
    private final Date endDate;
    private final BigDecimal amount;
    private final PromotionType type;
    private final Bitmap resource;

    public Promotion(PromotionApplicationType sku, String xyz, Date startDate, Date endDate, String amount, PromotionType percent, Bitmap resource) {
        this.appliesTo = sku;
        this.sku = xyz;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = new BigDecimal(amount);
        this.type = percent;
        this.resource = resource;
    }

    public boolean appliesTo(List<OrderedProduct> products) {
        Date now = new Date();
        if (now.before(startDate) || now.after(endDate)) {
            return false;
        }
        if (appliesTo == PromotionApplicationType.BASKET) {
            return true;
        }
        for (OrderedProduct product : products) {
            if (sku.equals(product.getSku())) {
                return true;
            }
        }
        return false;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PromotionType getType() {
        return type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Bitmap getResource() {
        return resource;
    }
}
