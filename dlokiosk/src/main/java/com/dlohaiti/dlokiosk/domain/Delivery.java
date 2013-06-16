package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.DeliveryType;

import java.util.Date;

public class Delivery {
    private final Integer id;
    private final int quantity;
    private final DeliveryType type;
    private final Date createdDate;

    public Delivery(int quantity, DeliveryType type, Date createdDate) {
        this(null, quantity, type, createdDate);
    }

    public Delivery(Integer id, Integer quantity, DeliveryType type, Date createdDate) {
        this.id = id;
        this.quantity = quantity;
        this.type = type;
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public DeliveryType getType() {
        return type;
    }
}
