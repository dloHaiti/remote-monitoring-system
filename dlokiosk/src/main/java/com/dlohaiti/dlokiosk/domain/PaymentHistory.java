package com.dlohaiti.dlokiosk.domain;

import java.util.Date;

public class PaymentHistory {
    private Long id;
    private String customerId;
    private Date paymentDate;
    private double amount;
    private Long receiptID;

    public PaymentHistory(Long id, String customerId, double amount, Date paymentDate) {
        this(id, customerId, amount, paymentDate, null);
    }

    public PaymentHistory(Long id, String customerId, double amount, Date paymentDate, Long receiptID) {
        this.id = id;
        this.amount = amount;
        this.customerId = customerId;
        this.receiptID = receiptID;
        this.paymentDate = paymentDate;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public Long getReceiptID() {
        return receiptID;
    }
}
