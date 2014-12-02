package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.db.ReceiptLineItemType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Receipt {
    private final Long id;
    private final List<LineItem> lineItems;
    private final Date createdDate;
    private final Double totalGallons;
    private final Money total;
    private final Long salesChannelId;
    private final String customerAccountId;
    private final String paymentMode;
    private final Boolean isSponsorSelected;
    private final String sponsorId;
    private final Money sponsorAmount;
    private final Money customerAmount;
    private final String paymentType;
    private final String deliveryTime;
    private PaymentHistory paymentHistory;

    public Receipt(List<LineItem> lineItems, Date createdDate, Double totalGallons, Money total) {
        this(null, lineItems, createdDate, totalGallons, total, null, null, null,
                null, null, null, null, null, null);

    }

    public Receipt(Long id, List<LineItem> lineItems, Date createdDate, Double totalGallons, Money total,
                   Long salesChannelId, String customerAccountId, String paymentMode,
                   Boolean isSponsorSelected, String sponsorId, Money sponsorAmount,
                   Money customerAmount, String paymentType, String deliveryTime) {
        this.id = id;
        this.lineItems = lineItems;
        this.createdDate = createdDate;
        this.totalGallons = totalGallons;
        this.total = total;
        this.salesChannelId = salesChannelId;
        this.customerAccountId = customerAccountId;
        this.paymentMode = paymentMode;
        this.isSponsorSelected = isSponsorSelected;
        this.sponsorId = sponsorId;
        this.sponsorAmount = sponsorAmount;
        this.customerAmount = customerAmount;
        this.paymentType = paymentType;
        this.deliveryTime = deliveryTime;
        this.paymentHistory=null;
    }

    public Receipt(List<LineItem> lineItems, Date createdDate, Double totalGallons, Money total,
                   long salesChannelId, String customerAccountId, String paymentMode,
                   boolean isSponsorSelected, String sponsorId, Money sponsorAmount,
                   Money customerAmount, String paymentType, String deliveryTime) {
        this(null, lineItems, createdDate, totalGallons, total, salesChannelId, customerAccountId, paymentMode,
                isSponsorSelected, sponsorId, sponsorAmount, customerAmount, paymentType, deliveryTime);
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    @JsonIgnore
    public Integer getLineItemsCount() {
        int total = 0;
        for (LineItem op : lineItems) {
            total += op.getQuantity();
        }
        return total;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public Double getTotalGallons() {
        return totalGallons;
    }

    public Money getTotal() {
        return total;
    }

    public Long getSalesChannelId() {
        return salesChannelId;
    }

    public String getCustomerAccountId() {
        return customerAccountId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public Boolean getIsSponsorSelected() {
        return isSponsorSelected;
    }

    public String getSponsorId() {
        if (sponsorId == null) {
            return "";
        }
        return sponsorId;
    }

    public PaymentHistory getPaymentHistory(){
        return paymentHistory;
    }

    public Money getSponsorAmount() {
        return sponsorAmount;
    }

    public Money getCustomerAmount() {
        return customerAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    @JsonIgnore
    public Integer getProductLineItemsCount() {
        int total = 0;
        for (LineItem op : lineItems) {
            if (op.getType() == ReceiptLineItemType.PRODUCT) {
                total += op.getQuantity();
            }
        }
        return total;
    }

    public List<LineItem> getProductLineItems() {
        List<LineItem> items = new ArrayList<LineItem>();
        for (LineItem item : lineItems) {
            if (item.getType() == ReceiptLineItemType.PRODUCT) {
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receipt receipt = (Receipt) o;

        if (isSponsorSelected != receipt.isSponsorSelected) return false;
        if (salesChannelId != receipt.salesChannelId) return false;
        if (createdDate != null ? !createdDate.equals(receipt.createdDate) : receipt.createdDate != null) return false;
        if (customerAccountId != null ? !customerAccountId.equals(receipt.customerAccountId) : receipt.customerAccountId != null)
            return false;
        if (customerAmount != null ? !customerAmount.equals(receipt.customerAmount) : receipt.customerAmount != null)
            return false;
        if (deliveryTime != null ? !deliveryTime.equals(receipt.deliveryTime) : receipt.deliveryTime != null)
            return false;
        if (id != null ? !id.equals(receipt.id) : receipt.id != null) return false;
        if (lineItems != null ? !lineItems.equals(receipt.lineItems) : receipt.lineItems != null) return false;
        if (paymentMode != null ? !paymentMode.equals(receipt.paymentMode) : receipt.paymentMode != null) return false;
        if (paymentType != null ? !paymentType.equals(receipt.paymentType) : receipt.paymentType != null) return false;
        if (sponsorAmount != null ? !sponsorAmount.equals(receipt.sponsorAmount) : receipt.sponsorAmount != null)
            return false;
        if (sponsorId != null ? !sponsorId.equals(receipt.sponsorId) : receipt.sponsorId != null) return false;
        if (total != null ? !total.equals(receipt.total) : receipt.total != null) return false;
        if (totalGallons != null ? !totalGallons.equals(receipt.totalGallons) : receipt.totalGallons != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (lineItems != null ? lineItems.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (totalGallons != null ? totalGallons.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (int) (salesChannelId ^ (salesChannelId >>> 32));
        result = 31 * result + (customerAccountId != null ? customerAccountId.hashCode() : 0);
        result = 31 * result + (paymentMode != null ? paymentMode.hashCode() : 0);
        result = 31 * result + (isSponsorSelected ? 1 : 0);
        result = 31 * result + (sponsorId != null ? sponsorId.hashCode() : 0);
        result = 31 * result + (sponsorAmount != null ? sponsorAmount.hashCode() : 0);
        result = 31 * result + (customerAmount != null ? customerAmount.hashCode() : 0);
        result = 31 * result + (paymentType != null ? paymentType.hashCode() : 0);
        result = 31 * result + (deliveryTime != null ? deliveryTime.hashCode() : 0);
        return result;
    }

    public void setPaymentHistory(PaymentHistory paymentHistory) {
        this.paymentHistory=paymentHistory;
    }
}
