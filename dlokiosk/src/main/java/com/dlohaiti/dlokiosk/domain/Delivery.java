package com.dlohaiti.dlokiosk.domain;

import com.dlohaiti.dlokiosk.DeliveryTrackingType;

import java.util.Date;

public class Delivery {
    private final Integer id;
    private final int quantity;
    private final DeliveryTrackingType type;
    private final Date createdAt;
    private final String kioskId;

    public Delivery(int quantity, DeliveryTrackingType type, Date createdAt, String kioskId) {
        this(null, quantity, type, createdAt, kioskId);
    }

    public Delivery(Integer id, Integer quantity, DeliveryTrackingType type, Date createdAt, String kioskId) {
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

    public DeliveryTrackingType getType() {
        return type;
    }
}
