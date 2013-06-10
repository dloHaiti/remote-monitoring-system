package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ReceiptLineItemType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Receipt {
    private final Long id;
    private final List<LineItem> lineItems;
    private final String kioskId;
    private final Date createdAt;
    private final Integer totalGallons;
    private final Money total;

    public Receipt(List<LineItem> lineItems, String kioskId, Date createdAt, Integer totalGallons, Money total) {
        this(null, lineItems, kioskId, createdAt, totalGallons, total);
    }

    public Receipt(Long id, List<LineItem> lineItems, String kioskId, Date createdAt, Integer totalGallons, Money total) {
        this.id = id;
        this.lineItems = lineItems;
        this.kioskId = kioskId;
        this.createdAt = createdAt;
        this.totalGallons = totalGallons;
        this.total = total;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    @JsonIgnore public Integer getLineItemsCount() {
        int total = 0;
        for (LineItem op : lineItems) {
            total += op.getQuantity();
        }
        return total;
    }

    public Date getCreatedDate() {
        return createdAt;
    }

    //TODO: infer on server side from authentication
    public String getKioskId() {
        return kioskId;
    }

    @JsonIgnore public Long getId() {
        return id;
    }

    public Integer getTotalGallons() {
        return totalGallons;
    }

    public Money getTotal() {
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receipt receipt = (Receipt) o;

        if (createdAt != null ? !createdAt.equals(receipt.createdAt) : receipt.createdAt != null) return false;
        if (id != null ? !id.equals(receipt.id) : receipt.id != null) return false;
        if (kioskId != null ? !kioskId.equals(receipt.kioskId) : receipt.kioskId != null) return false;
        if (lineItems != null ? !lineItems.equals(receipt.lineItems) : receipt.lineItems != null) return false;
        if (total != null ? !total.equals(receipt.total) : receipt.total != null) return false;
        if (totalGallons != null ? !totalGallons.equals(receipt.totalGallons) : receipt.totalGallons != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (lineItems != null ? lineItems.hashCode() : 0);
        result = 31 * result + (kioskId != null ? kioskId.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (totalGallons != null ? totalGallons.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        return result;
    }

    @JsonIgnore public Integer getProductLineItemsCount() {
        int total = 0;
        for (LineItem op : lineItems) {
            if(op.getType() == ReceiptLineItemType.PRODUCT) {
                total += op.getQuantity();
            }
        }
        return total;
    }

    public List<LineItem> getProductLineItems() {
        List<LineItem> items = new ArrayList<LineItem>();
        for(LineItem item : lineItems) {
            if(item.getType() == ReceiptLineItemType.PRODUCT) {
                items.add(item);
            }
        }
        return items;
    }
}
