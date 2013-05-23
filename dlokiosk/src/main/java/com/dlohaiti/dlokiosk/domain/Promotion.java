package com.dlohaiti.dlokiosk.domain;

import android.graphics.Bitmap;
import com.dlohaiti.dlokiosk.VisibleGridItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Promotion implements VisibleGridItem {
    private final Long id;
    private final PromotionApplicationType appliesTo;
    private final String sku;
    private final Date startDate;
    private final Date endDate;
    private final BigDecimal amount;
    private final PromotionType type;
    private final Bitmap resource;

    public Promotion(Long id, PromotionApplicationType appliesTo, String sku, Date startDate, Date endDate, String amount, PromotionType percent, Bitmap resource) {
        this.id = id;
        this.appliesTo = appliesTo;
        this.sku = sku;
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

    @Override
    public Long getId() {
        return id;
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

    @Override
    public Bitmap getImageResource() {
        return resource;
    }
}
