package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.DeliveryType;

import java.util.Date;

public class Delivery {
    private final Integer id;
    private final int quantity;
    private final DeliveryType type;
    private final Date createdAt;
    private final String kioskId;

    public Delivery(int quantity, DeliveryType type, Date createdAt, String kioskId) {
        this(null, quantity, type, createdAt, kioskId);
    }

    public Delivery(Integer id, Integer quantity, DeliveryType type, Date createdAt, String kioskId) {
        this.id = id;
        this.quantity = quantity;
        this.type = type;
        this.createdAt = createdAt;
        this.kioskId = kioskId;
    }

    public Integer getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getKioskId() {
        return kioskId;
    }

    public int getQuantity() {
        return quantity;
    }

    public DeliveryType getType() {
        return type;
    }
}
